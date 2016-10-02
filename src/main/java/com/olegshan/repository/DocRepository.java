package com.olegshan.repository;

import com.olegshan.entity.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by olegshan on 02.10.2016.
 */
@Repository
public interface DocRepository extends JpaRepository<Doc, String> {
}
