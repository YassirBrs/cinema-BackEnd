package com.cinema.service;

import com.cinema.dao.*;
import com.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaServiceImpl implements ICinemaService {
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Override
    public void initVilles() {
        Stream.of("Agadir","Casablanca","Marrakech","Rabat","Tanger").forEach(v->{
            Ville ville=new Ville();
            ville.setName(v);
            villeRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(v->{
            Stream.of("Rialto","Megarama","IMAX","Chahrazad").forEach(c->{
                Cinema cinema=new Cinema();
                cinema.setName(c);
                cinema.setNbSalles(5+(int)(Math.random()*5));
                cinema.setVille(v);
                cinemaRepository.save(cinema);
            });
        });
    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema -> {
            for (int i=1;i<=cinema.getNbSalles();i++){
                Salle salle=new Salle();
                salle.setName("Salle "+i);
                salle.setCinema(cinema);
                salle.setNbPlace(15+(int)(Math.random()*20));
                salleRepository.save(salle);
            }
        });
    }

    @Override
    public void initSeances() {
        DateFormat dateFormat=new SimpleDateFormat("HH:mm");
        Stream.of("13:00","15:00","17:00","19:00","21:00","23:00").forEach(s->{
            Seance seance=new Seance();
            try {
                seance.setHeureDebut(dateFormat.parse(s));
                seanceRepository.save(seance);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle -> {
            for (int i=0;i<salle.getNbPlace();i++){
                Place place=new Place();
                place.setNumero(i+1);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });
    }

    @Override
    public void initCategories() {
        Stream.of("Action","Comedy","Drama","Fiction","Suspence","Crime","Fantasie","Sport","Romance","Adventure").forEach(v->{
            Category category=new Category();
            category.setName(v);
            categoryRepository.save(category);
        });
    }

    @Override
    public void initFilms() {
        double[] durees=new double[]{1,1.5,2,2.5,3};
        List<Category> category=categoryRepository.findAll();
        Stream.of("Spider Man","Parasite","Joker","Knives Out","1917","Inception","Interstellar","Bloodshot").forEach(v->{
            Film film=new Film();
            film.setTitre(v);
            film.setDuree(durees[new Random().nextInt(durees.length)]);
            film.setPhoto(v.replaceAll(" ","")+".jpg");
            film.setCategory(category.get(new Random().nextInt(category.size())));
            filmRepository.save(film);
        });
    }

    @Override
    public void initProjections() {
        double[] prices=new double[]{30,50,60,70,80,90};
        List<Film> films=filmRepository.findAll();
        villeRepository.findAll().forEach(v->{
            v.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle -> {
                    int index=new Random().nextInt(films.size());
                    Film f=films.get(index);
//                    filmRepository.findAll().forEach(film -> {
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection=new Projection();
                            projection.setDateProjection(new Date());
                            projection.setFilm(f);
                            projection.setPrix(prices[new Random().nextInt(prices.length)]);
                            projection.setSalle(salle);
                            projection.setSeance(seance);
                            projectionRepository.save(projection);
                        });
//                    });
                });
            });
        });
    }

    @Override
    public void initTickets() {
        projectionRepository.findAll().forEach(projection -> {
            projection.getSalle().getPlaces().forEach(place -> {
                Ticket ticket=new Ticket();
                ticket.setPlace(place);
                ticket.setProjection(projection);
                ticket.setPrix(projection.getPrix());
                ticket.setReserve(false);
                ticketRepository.save(ticket);
            });
        });
    }
}
