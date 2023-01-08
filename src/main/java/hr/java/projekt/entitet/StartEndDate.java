package hr.java.projekt.entitet;

import java.io.Serializable;
import java.time.LocalDate;

public record StartEndDate(LocalDate startDate, LocalDate endDate) implements Serializable {}
