package com.project.vetProject.dto.request.availableDate;

import com.project.vetProject.entities.Doctor;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.Doc;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateSaveRequest {
    @NotNull(message = "Müsait gün boş olamaz")
    private LocalDate date;
    private Doctor doctor;
}
