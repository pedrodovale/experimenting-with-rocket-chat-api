package com.pedrodovale.experimentingwith.rocketchat.model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
  private String id;
  @NotBlank private String title;
  @NotBlank private String author;
  @NotNull private LocalDate publicationDate;
}
