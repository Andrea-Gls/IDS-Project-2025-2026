package gasing.hackathon.repository;

import gasing.hackathon.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // crea un'istanza di questa classe all'avvio e tienila pronta per essere iniettata (Autowired) nei Service

public interface UserRepository extends JpaRepository<User, Long> { // ereditiamo tutti i metodi per db (User è la tabella, Long è la primary key)

    // optional serve per costringe a controllare se l'utente esiste prima di usarlo cosi evitiamo errore di nullpointerexception
    public Optional<User> findByEmail(String email); //serve per fare la query al db in automatico con spring

    public boolean existsByEmail(String email); // controlla se esiste già

    //il metodo save() lo prende dalla JpaRepository
}
