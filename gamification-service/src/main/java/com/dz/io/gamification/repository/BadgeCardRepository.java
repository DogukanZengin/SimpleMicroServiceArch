package com.dz.io.gamification.repository;

import com.dz.io.gamification.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BadgeCardRepository extends CrudRepository<BadgeCard, Long> {

    List<BadgeCard> findByUserIdOrderByBadgeTimeStampDesc(final Long userId);
}
