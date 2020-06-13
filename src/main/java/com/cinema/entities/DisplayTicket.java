package com.cinema.entities;

import org.springframework.data.rest.core.config.Projection;

import java.util.Collection;
import java.util.Date;

@Projection(name = "p2",types = {com.cinema.entities.Ticket.class})
public interface DisplayTicket {
    public int getId();
    public String getNomClient();
    public double getPrix();
    public Integer getCodePaiment();
    public boolean getReserve();
    public Place getPlace();
}
