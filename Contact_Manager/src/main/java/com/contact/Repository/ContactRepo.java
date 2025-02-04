package com.contact.Repository;
/*
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.entity.Contact;

public interface ContactRepo extends JpaRepository<Contact, Long> {

    @Query("from Contact as c where c.user.id = :userId")
    public Page<Contact> findContactsByUserId(@Param("userId") int userId, Pageable pegable);
	//public Page<Contact> findContactByUser(int id, PageRequest pageable);

    // Adjusted query method to use the correct field name "Name"
   // public List<Contact> findByNameContainingAndUser(String name, User user);

   // public List<Contact> findByUserAndNameContaining(User user, String query);

}
*/
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.entity.Contact;
import com.contact.entity.User;

import java.util.List;

public interface ContactRepo extends JpaRepository<Contact, Long> {

    @Query("from Contact as c where c.user.id =:userId")
    Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);

    // Search contact
   // List<Contact> findByNameContainingAndUser(String keywords, User user);
}