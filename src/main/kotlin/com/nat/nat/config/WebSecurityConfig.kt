package com.nat.nat.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import java.lang.Exception
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    protected lateinit var datasource: DataSource

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
            .authorizeRequests()
                .antMatchers("/", "/home", "/registration").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .defaultSuccessUrl("/hello", true)
                .loginPage("/login")
                .permitAll()
            .and()
                .logout()
                .permitAll()
    }


    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication()
            .dataSource(this.datasource)
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
            .usersByUsernameQuery("select username, password, is_active from usr where username=?")
            .authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?")
    }
}