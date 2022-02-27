package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.enumerations.EventType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Event implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long eventId;
	
	String eventName;
	
	@Temporal(TemporalType.DATE)
	Date createdAt;
	
	String description;
	
	@Enumerated(EnumType.STRING)
	EventType eventType;
	
	String link;
	
	String place;
	
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "joinedEvents")
	Set<User> participants;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
	Set<Donation> donations;
	
	

}
