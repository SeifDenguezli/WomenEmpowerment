package tn.esprit.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Event;
import tn.esprit.spring.entities.User;
@Repository
public interface EventRepo  extends JpaRepository<Event , Long> {
	@Query(nativeQuery = true , value = "select count(user_user_id) from user_created_events where  user_user_id =: id")
	public Long findUserDonationsById(@Param("id") Long id);
	@Query(value="select sum(d.amount) from donation d join user_created_events u on d.event_event_id = u.created_events_event_id where u.user_user_id=:id",nativeQuery = true)
	public float calcultotaldonationsByUser(@Param("id")Long id);
	@Query(value="select MAX(d.amount) from donation d join user_created_events u on d.event_event_id = u.created_events_event_id where u.user_user_id=1",nativeQuery = true)
	public float maxDonationByUser(@Param("id")Long id);
	/*@Query(value="SELECT event_event_id from donation group by event_event_id order by count(transaction_transactionid) desc limit 1", nativeQuery=true)
	public Long retrieveMaxEventTransactioned();*/
	@Query(value="SELECT  joined_events_event_id from user_joined_events group by joined_events_event_id order by count(participants_user_id) desc limit 1", nativeQuery=true)
	public Long retrieveMaxJoinedEvent();
	
	@Query(value="select u.user_id from users u where role = 'COMPANY'" ,nativeQuery=true)
	public List<Long> GET_LIST_CAMPANY();
	
	
	
	@Query(value="SELECT  d.donor_user_id,sum(d.amount_for_event)	 FROM donation d join users on users.user_id = donor_user_id order by 2 desc limit 1",nativeQuery=true)
	public List<Long> GET_ID_BEST_DONNER();
	
	}


