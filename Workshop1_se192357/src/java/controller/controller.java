/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ProjectDAO;
import dao.UserDAO;
import dto.ProjectDTO;
import dto.UserDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // Import HttpSession

/**
 *
 * @author PC
 */
@WebServlet(name = "controller", urlPatterns = {"/controller"})
public class controller extends HttpServlet {

    private static final String LOGIN_PAGE = "login.jsp";
    private static final String DASHBOARD_PAGE = "dashboard.jsp";

    public UserDTO getUser(String Username) {
        UserDAO udao = new UserDAO();
        UserDTO user = udao.readById(Username);
        return user;
    }

    public boolean isValidLogin(String strUserID, String strPassword) {
        UserDTO user = getUser(strUserID);
        System.out.println(user);
//        System.out.println(user.getPassword());
        System.out.println(strPassword);
        return user != null && user.getPassword().equals(strPassword);
    }

    public void search(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        if (searchTerm == null) {
            searchTerm = "";
        }
        ProjectDAO projectDAO = new ProjectDAO(); // Create an instance of ProjectDAO
        List<ProjectDTO> projects = projectDAO.search(searchTerm); // Call the instance method
        request.setAttribute("projects", projects);
        request.setAttribute("searchTerm", searchTerm);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        try {
            String action = request.getParameter("action");
            if (action == null) {
                url = LOGIN_PAGE;
            } else {
                switch (action) {
                    case "login":
                        String strUserID = request.getParameter("username");
                        String strPassword = request.getParameter("password");
                        if (isValidLogin(strUserID, strPassword)) {
                            url = DASHBOARD_PAGE;
                            UserDTO user = getUser(strUserID);
                            request.getSession().setAttribute("user", user);
                            
                            // search
                            search(request, response);
                        } else {
                            request.setAttribute("error", "Incorrect UserID or Password");
                            url = LOGIN_PAGE;
                        }   break;
                    case "logout":
                        {
                            HttpSession session = request.getSession();
                            if (session.getAttribute("user") != null) {
                                request.getSession().invalidate(); // Hủy bỏ session
                                url = LOGIN_PAGE;
                                request.setAttribute("error", "You have logged out successfully.");
                            }       break;
                        }
                    case "search":
                        {
                            String searchTerm = request.getParameter("searchTerm");
                            ProjectDAO projectDao = new ProjectDAO();
                            List<ProjectDTO> projects = projectDao.search(searchTerm);
                            request.setAttribute("projects", projects);
                            HttpSession session = request.getSession(false);
                            if (session != null && session.getAttribute("user") != null) {
                                url = DASHBOARD_PAGE;
                            } else {
                                url = LOGIN_PAGE;
                            }       break;
                        }
                    case "createProject":
                        {
                            int projectID = Integer.parseInt(request.getParameter("projectID"));
                            String projectName = request.getParameter("projectName");
                            String description = request.getParameter("description");
                            String status = request.getParameter("status");
                            Date estimatedLaunch = Date.valueOf(request.getParameter("estimatedLaunch"));
                            ProjectDTO project = new ProjectDTO(projectID, projectName, description, status, estimatedLaunch);
                            ProjectDAO projectDAO = new ProjectDAO();
                            boolean created = projectDAO.create(project);
                            HttpSession session = request.getSession(false);
                            if (session != null && session.getAttribute("user") != null) {
                                if (created) {
                                    request.setAttribute("message", "Project created successfully!");
                                } else {
                                    request.setAttribute("message", "Project creation failed.");
                                }
                                ProjectDAO dao = new ProjectDAO();
                                List<ProjectDTO> list = dao.readAll();
                                request.setAttribute("projects", list);
                                url = DASHBOARD_PAGE;
                            } else {
                                url = LOGIN_PAGE;
                            }       break;
                        }
                    case "updateProject":
                        {
                            int projectID = Integer.parseInt(request.getParameter("projectID"));
                            String projectName = request.getParameter("projectName");
                            String description = request.getParameter("description");
                            String status = request.getParameter("status");
                            Date estimatedLaunch = Date.valueOf(request.getParameter("estimatedLaunch"));
                            ProjectDTO project = new ProjectDTO(projectID, projectName, description, status, estimatedLaunch);
                            ProjectDAO projectDAO = new ProjectDAO();
                            boolean updated = projectDAO.update(project);
                            HttpSession session = request.getSession(false);
                            if (session != null && session.getAttribute("user") != null) {
                                if (updated) {
                                    request.setAttribute("message", "Project updated successfully!");
                                } else {
                                    request.setAttribute("message", "Project update failed.");
                                }
                                ProjectDAO dao = new ProjectDAO();
                                List<ProjectDTO> list = dao.readAll();
                                request.setAttribute("projects", list);
                                url = DASHBOARD_PAGE;
                            }       break;
                        }
                    default:
                        break;
                }
            }

        } catch (IOException | NumberFormatException | ServletException e) { //  Better error handling (log the exception)
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            url = LOGIN_PAGE; // Or an error page
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
