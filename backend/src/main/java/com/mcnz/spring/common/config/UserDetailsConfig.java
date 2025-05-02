package com.mcnz.spring.common.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mcnz.spring.membership.UserOrganizationRole;
import com.mcnz.spring.membership.UserOrganizationRoleRepository;
import com.mcnz.spring.user.User;
import com.mcnz.spring.user.UserRepository;

@Configuration
public class UserDetailsConfig {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserOrganizationRoleRepository userOrgRoleRepository;

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

            List<UserOrganizationRole> memberships = userOrgRoleRepository.findByUser_Id(user.getId());

            Map<String, List<String>> organizationRolesMap = memberships.stream()
                    .collect(Collectors.groupingBy(
                            membership -> membership.getOrganization().getId().toString(),
                            Collectors.mapping(membership -> membership.getRole().getName().name(),
                                    Collectors.toList())));

            // Using format "ROLE_ROLENAME_ORG_ORGID" for Spring Security context
            List<GrantedAuthority> authorities = memberships.stream()
                    .map(membership -> {
                        String roleName = membership.getRole().getName().name();
                        String orgId = membership.getOrganization().getId().toString();
                        String authorityString = "ROLE_" + roleName + "_ORG_" + orgId;
                        return new SimpleGrantedAuthority(authorityString);
                    })
                    .collect(Collectors.toList());

            user.setAuthorities(authorities);
            user.setOrganizationRolesMap(organizationRolesMap);

            return user;
        };
    }
}