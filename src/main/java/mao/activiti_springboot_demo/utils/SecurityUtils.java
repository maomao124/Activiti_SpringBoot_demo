package mao.activiti_springboot_demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Project name(项目名称)：Activiti-SpringBoot-demo
 * Package(包名): mao.activiti_springboot_demo.utils
 * Class(类名): SecurityUtils
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/10/19
 * Time(创建时间)： 15:53
 * Version(版本): 1.0
 * Description(描述)： 无
 */

@Component
public class SecurityUtils
{

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    @Autowired
    @Qualifier("myUserDetailsService")
    private UserDetailsService userDetailsService;

    public void logInAs(String username)
    {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user == null)
        {
            throw new IllegalStateException("User " + username + " doesn't exist, please provide a valid user");
        }
        log.info("> Logged in as: " + username);

        SecurityContextHolder.setContext(
                new SecurityContextImpl(
                        new Authentication()
                        {
                            @Override
                            public Collection<? extends GrantedAuthority> getAuthorities()
                            {
                                return user.getAuthorities();
                            }

                            @Override
                            public Object getCredentials()
                            {
                                return user.getPassword();
                            }

                            @Override
                            public Object getDetails()
                            {
                                return user;
                            }

                            @Override
                            public Object getPrincipal()
                            {
                                return user;
                            }

                            @Override
                            public boolean isAuthenticated()
                            {
                                return true;
                            }

                            @Override
                            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException
                            {
                            }

                            @Override
                            public String getName()
                            {
                                return user.getUsername();
                            }
                        }));
        org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(username);
    }
}
