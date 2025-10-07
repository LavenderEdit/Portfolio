package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Joan Lavender
 */
public interface ContactMessageRepo extends JpaRepository<ContactMessage, Long> {
}
