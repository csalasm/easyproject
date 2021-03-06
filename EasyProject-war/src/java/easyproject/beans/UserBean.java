/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyproject.beans;


import EasyProject.ejb.ProyectoFacade;
import EasyProject.ejb.TareaFacade;
import EasyProject.ejb.UsuarioFacade;
import EasyProject.entities.Proyecto;
import EasyProject.entities.Tarea;
import EasyProject.entities.Usuario;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author victo
 */
@ManagedBean
@SessionScoped
public class UserBean {
    @EJB
    private ProyectoFacade proyectoFacade;
    @EJB
    private TareaFacade tareaFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    
    

   
    private String email;
    private Usuario user;
    private String name;
    private String image;
    protected Proyecto projectSelected = null;
    protected Tarea taskSelected = null;
    
    
    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Proyecto getProjectSelected() {
        return projectSelected;
    }

    public void setProjectSelected(Proyecto projectSelected) {
        this.projectSelected = projectSelected;
    }

    public Tarea getTaskSelected() {
        return taskSelected;
    }

    public void setTaskSelected(Tarea taskSelected) {
        
        this.taskSelected = taskSelected;
    }
 
    public String doProfile(){
        return "Profile";
    }
    public String doCheckUser(){
   
        if( (usuarioFacade.find(user)!=null)){   
            return "User";
        }
        else{
            user=new Usuario();
            user.setEmail(email);
            usuarioFacade.create(user);
        }
        return "User";
    } 
    
    public void doLogin(){
        projectSelected = null;
        name = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name");
        email = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("email");
        image = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("image");
        
        if (image.equals("")) {
            image = "https://freeiconshop.com/files/edd/person-flat.png";
        }
        
        if(usuarioFacade.getUser(email) == null){
            usuarioFacade.setNewUser(email, name);
            user = usuarioFacade.getUser(email);
        }else{
            user = usuarioFacade.getUser(email);
        } 
    }

    public String doSignOut(){
        HttpSession session =  (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if(session != null){
            session.invalidate();
        }
        user = new Usuario();
        name="";
        email="";
        image="";
        projectSelected = new Proyecto();
        taskSelected = new Tarea();
        return "PageTitle";
    }
        
    public String doGoToMainPage () {
        return "MainPage";
    }
    
    

}
