package com.xsalefter.hipsterapp.repository;

import com.xsalefter.hipsterapp.domain.City;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the City entity.
 */
@Repository
public interface CityRepository extends JpaRepository<City,Long> {

}
