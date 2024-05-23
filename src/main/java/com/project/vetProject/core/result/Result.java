package com.project.vetProject.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Result {
    private boolean status;  // İşlemin başarılı olup olmadığını gösterir
    private String message;  // İşlemle ilgili mesaj
    private String code;     // İşlemle ilgili durum kodu
}
