package com.cinema.entities;

import org.springframework.data.rest.core.config.Projection;

import java.util.Collection;
import java.util.Date;

@Projection(name = "p1",types = {com.cinema.entities.Projection.class})
public interface DisplayProjection {
    public int getId();
    public double getPrix();
    public Date getDateProjection();
    public Film getFilm();
    public Salle getSalle();
    public Seance getSeance();
    public Collection<Ticket> getTickets();
}
