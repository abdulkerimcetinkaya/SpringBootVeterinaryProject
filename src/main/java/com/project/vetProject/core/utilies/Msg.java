package com.project.vetProject.core.utilies;

// Uygulama genelinde kullanılan mesajları içeren yardımcı sınıf
public class Msg {
    public static final String CREATED = "Kayıt eklendi.";  // Kayıt ekleme mesajı
    public static final String VALIDATE_ERROR = "Veri Doğrulama Hatası.";  // Doğrulama hatası mesajı
    public static final String OK = "İşlem Başarılı.";  // Başarılı işlem mesajı
    public static final String NOT_FOUND = "Veri Bulunamadı.";  // Veri bulunamadı mesajı
    public static final String NOT_FOUND_BY_NAME = "Bu İsme Ait Bir Veri Bulunmamaktadır.";  // İsimle ilgili veri bulunamadı mesajı
    public static final String FOUND_BY_NAME = "Veritabanında Aynı Veri Bulunmaktadır.";  // Aynı veri bulunmakta mesajı

    // Belirli bir entity sınıfı için özelleştirilmiş mesaj döner
    public static String getEntityForMsg(Class<?> entity) {
        return entity.getSimpleName() + " Tablosunda Aynı Veri Bulunmaktadır.";
    }
}
