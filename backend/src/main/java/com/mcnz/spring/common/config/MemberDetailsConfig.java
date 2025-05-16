package com.mcnz.spring.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mcnz.spring.member.Member;
import com.mcnz.spring.member.MemberRepository;
import com.mcnz.spring.membership.MemberOrganizationRoleRepository;
import com.mcnz.spring.membership.MemberOrganizationRoleRepository.MemberRolesProjection;

@Configuration
public class MemberDetailsConfig {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberOrganizationRoleRepository memberOrgRoleRepository;

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

            List<MemberRolesProjection> membershipProjections = memberOrgRoleRepository
                    .findRolesByMemberId(member.getMemberId());

            Map<String, Map<String, String>> orgRoleAndPositionMap = new HashMap<>();
            if (membershipProjections != null) {
                orgRoleAndPositionMap = membershipProjections.stream()
                        .collect(Collectors.toMap(
                                projection -> projection.getOrganizationId().toString(),
                                projection -> {
                                    Map<String, String> details = new HashMap<String, String>();
                                    details.put("role", projection.getRole());
                                    details.put("organizationName", projection.getOrganizationName());
                                    return details;
                                },
                                (existingValue, newValue) -> newValue));
            }

            List<GrantedAuthority> authorities = new ArrayList<>();
            if (membershipProjections != null) {
                authorities = membershipProjections.stream()
                        .map(projection -> {
                            String roleName = projection.getRole();
                            String orgId = projection.getOrganizationId().toString();
                            String authorityString = roleName + "_ORG_" + orgId;
                            return new SimpleGrantedAuthority(authorityString);
                        })
                        .distinct()
                        .collect(Collectors.toList());
            }

            member.setAuthorities(authorities);
            member.setOrganizationRolesMap(orgRoleAndPositionMap);
            return member;
        };
    }
}