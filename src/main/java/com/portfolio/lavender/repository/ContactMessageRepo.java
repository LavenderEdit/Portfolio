package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Studios TKOH!
 */
public interface ContactMessageRepo extends JpaRepository<ContactMessage, Long> {
}
