package com.project.vetProject.service.concretes;

import com.project.vetProject.service.abstracts.IAnimalService;
import com.project.vetProject.service.abstracts.IAppointmentService;
import com.project.vetProject.service.abstracts.IDoctorService;
import com.project.vetProject.core.config.ConvertEntityToResponse;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.exception.DataAlreadyExistException;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.repository.AppointmentRepo;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.appointment.AppointmentSaveRequest;
import com.project.vetProject.dto.request.appointment.AppointmentUpdateRequest;
import com.project.vetProject.dto.response.appointment.AppointmentResponse;
import com.project.vetProject.entities.Animal;
import com.project.vetProject.entities.Appointment;
import com.project.vetProject.entities.Doctor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentManager implements IAppointmentService {
    private final AppointmentRepo appointmentRepo;
    private final IAnimalService animalService;
    private final IDoctorService doctorService;
    private final IModelMapperService modelMapperService;
    private final ConvertEntityToResponse<Appointment, AppointmentResponse> convert;

    // Yeni bir randevu kaydeder
    @Override
    public ResultData<AppointmentResponse> save(AppointmentSaveRequest appointmentSaveRequest) {
        LocalDateTime dateTime = appointmentSaveRequest.getDateTime();
        // Dakikalar 00 değilse hata mesajı veriyor
        if (dateTime.getMinute() != 0){
            return ResultHelper.error("Lütfen dakika bilgisini '00' giriniz.");
        }

        // Aynı tarih, doktor ve hayvan için varolan bir randevu olup olmadığını kontrol eder
        Optional<Appointment> appointmentOptional = this.findByValueForValid(
                appointmentSaveRequest.getDateTime(),
                appointmentSaveRequest.getDoctorId(),
                appointmentSaveRequest.getAnimalId()
        );
        if (appointmentOptional.isPresent()){
            throw new DataAlreadyExistException(Msg.getEntityForMsg(Appointment.class));
        }

        // AnimalId ve DoctorId'ye göre nesneler üretiliyor
        Animal animal = this.animalService.get(appointmentSaveRequest.getAnimalId());
        Doctor doctor = this.doctorService.get(appointmentSaveRequest.getDoctorId());

        // Doktorun müsait günlerini liste içerisine atıyor
        List<Doctor> doctorList =  this.doctorService.findByIdAndAvailableDateDate(appointmentSaveRequest.getDoctorId(), LocalDate.from(dateTime));

        // Oluşturulan randevular içerisinde çakışan randevuları liste içerisine atıyor
        List<Appointment> appointmentByDate = this.findByDateTime(dateTime);

        // DoctorId ve AnimalId'ler aynı olduğu için update işlemi yapmasın diye null değeri verilir
        appointmentSaveRequest.setAnimalId(null);
        appointmentSaveRequest.setDateTime(null);
        appointmentSaveRequest.setDoctorId(null);

        // Rest API ile setleme işlemi yapılır
        Appointment saveAppointment = this.modelMapperService.forRequest().map(appointmentSaveRequest, Appointment.class);
        saveAppointment.setAnimal(animal);
        saveAppointment.setDoctor(doctor);
        saveAppointment.setDateTime(dateTime);

        // Liste içerisine aldığımız değerlerden çakışan varsa hata mesajı fırlatır, yoksa veritabanına kaydetme işlemi yapar
        if (doctorList.isEmpty()){
            return ResultHelper.error("Doktor bu tarihte müsait değildir.");
        } else if (!appointmentByDate.isEmpty()) {
            return ResultHelper.error("Doktorun bu saatte randevusu bulunmaktadır.");
        } else {
            return ResultHelper.created(this.modelMapperService.forResponse().map(this.appointmentRepo.save(saveAppointment), AppointmentResponse.class));
        }
    }

    // Belirli bir kimliğe sahip randevuyu getirir
    @Override
    public Appointment get(int id) {
        return this.appointmentRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    // Sayfalama kullanarak randevuları getirir
    @Override
    public ResultData<CursorResponse<AppointmentResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Appointment> appointmentPage = this.appointmentRepo.findAll(pageable);
        Page<AppointmentResponse> appointmentResponsePage = appointmentPage.map(appointment -> this.modelMapperService.forResponse().map(appointment, AppointmentResponse.class));
        return ResultHelper.cursor(appointmentResponsePage);
    }

    // Randevuyu günceller
    @Override
    public ResultData<AppointmentResponse> update(AppointmentUpdateRequest appointmentUpdateRequest) {
        this.get(appointmentUpdateRequest.getId());
        Appointment updateAppointment = this.modelMapperService.forRequest().map(appointmentUpdateRequest, Appointment.class);
        return ResultHelper.success(this.modelMapperService.forResponse().map(this.appointmentRepo.save(updateAppointment), AppointmentResponse.class));
    }

    // Belirli bir kimliğe sahip randevuyu siler
    @Override
    public boolean delete(int id) {
        Appointment appointment = this.get(id);
        this.appointmentRepo.delete(appointment);
        return true;
    }

    // Belirli bir tarih ve saatteki randevuları bulur
    @Override
    public List<Appointment> findByDateTime(LocalDateTime localDateTime) {
        return this.appointmentRepo.findByDateTime(localDateTime);
    }

    // Doktor kimliği ve tarih aralığına göre randevuları bulur
    @Override
    public ResultData<List<AppointmentResponse>> findByDoctorIdAndDateTimeBetween(int id, LocalDate entryDate, LocalDate exitDate) {
        LocalDateTime convertedEntryDate = entryDate.atStartOfDay();
        LocalDateTime convertedExitDate = exitDate.atStartOfDay().plusDays(1);
        List<Appointment> appointmentList = this.appointmentRepo.findByDoctorIdAndDateTimeBetween(id, convertedEntryDate, convertedExitDate);
        List<AppointmentResponse> appointmentResponseList = this.convert.convertToResponseList(appointmentList, AppointmentResponse.class);
        return ResultHelper.success(appointmentResponseList);
    }

    // Hayvan kimliği ve tarih aralığına göre randevuları bulur
    @Override
    public ResultData<List<AppointmentResponse>> findByAnimalIdAndDateTimeBetween(int id, LocalDate entryDate, LocalDate exitDate) {
        LocalDateTime convertedEntryDate = entryDate.atStartOfDay();
        LocalDateTime convertedExitDate = exitDate.atStartOfDay().plusDays(1);
        List<Appointment> appointmentList = this.appointmentRepo.findByAnimalIdAndDateTimeBetween(id, convertedEntryDate, convertedExitDate);
        List<AppointmentResponse> appointmentResponseList = this.convert.convertToResponseList(appointmentList, AppointmentResponse.class);
        return ResultHelper.success(appointmentResponseList);
    }

    // Belirli bir tarih, doktor kimliği ve hayvan kimliğine göre randevuyu bulur
    @Override
    public Optional<Appointment> findByValueForValid(LocalDateTime dateTime, Integer doctorId, Integer animalId) {
        return this.appointmentRepo.findByDateTimeAndDoctorIdAndAnimalId(dateTime, doctorId, animalId);
    }
}
