import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ting.he4@pactera.com
 * @date 2018年12月14日 - 17:08
 * @history 2018年12月14日 - 17:08 ting.he4@pactera.com  create.
 */
public class Tutorial {
    private static final transient Logger log = LoggerFactory.getLogger(Tutorial.class);
    public static void main(String[] args) {
        log.info("My First Apache Shiro Application");
        //1.
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //2.
        SecurityManager instance = factory.getInstance();
        //3.
        SecurityUtils.setSecurityManager(instance);

        //4.获取用户，subject
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute("hello","world");
        String value = (String) session.getAttribute("hello");
        if (!"world".equals(value)) {
            log.info("save session attribute error");
        }

        //5.判断当前用户是否登录
        if (!currentUser.isAuthenticated()) {
            //UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            UsernamePasswordToken token = new UsernamePasswordToken("darkhelmet", "ludicrousspeed");
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException e) {
                log.error("account not exist", e);
            } catch (IncorrectCredentialsException e) {
                log.error("incorrect account num or password" ,e);
            } catch (LockedAccountException e) {
                log.error("account was locked", e);
            } catch (AuthenticationException e) {
                log.error("account was authenticated failed", e);
            }
        }

        //获取当前登录用户名
        log.info("User [" + currentUser.getPrincipal() + "] logining successfully");
        //判断当前用户是否有特定的角色
        if (currentUser.hasRole("goodguy")) {
            log.info("may the goodguy be with you");
        } else if (currentUser.hasRole("schwartz")){
            log.info("may the schwartz be with you");
        } else {
            log.info("hello guest");
        }

        //判断当前用户是否有权限在一个确定类型的实体上进行操作
        if (currentUser.isPermitted("hello:jack")) {
            log.info("hello jack welcome back");
        } else if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("hello, you can drive this car");
        } else if (currentUser.isPermitted("lightsaber:hi")){
            log.info("hi, welcome, my super hero");
        } else {
            log.info("hello, welcome to my world");
        }

        currentUser.logout();

        System.exit(0);
    }
}
