package lk.jiat.app.security.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.jiat.app.security.model.User;

import java.util.Set;

@RequestScoped
public class LoginService {

    @PersistenceContext
    private EntityManager em;

    public boolean validate(String username , String password){
            User user = em.find(User.class , username);
            return user != null && user.getPassword().equals(password);

    }

    public Set<String> getRoles(String username){
        User user = em.find(User.class , username);
        return user != null ? user.getRoles() : Set.of();
    }

    public User getUser(String username){
        return em.find(User.class , username);
    }

}
