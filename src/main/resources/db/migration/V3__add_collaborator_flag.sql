/**
 * Author:  Studios TKOH!
 * Created: Nov 12, 2025
 */
USE `studiostkoh.portafolio`;

ALTER TABLE `profile`
ADD COLUMN `is_tkoh_collaborator` BOOLEAN NOT NULL DEFAULT FALSE
AFTER `resume_url`;