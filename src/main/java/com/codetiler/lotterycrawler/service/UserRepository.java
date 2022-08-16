package com.codetiler.lotterycrawler.service;

import com.codetiler.lotterycrawler.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SysUser,Long> {
}
