/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dao.UserDAO;
import dto.UserDTO;
import java.util.List;

/**
 *
 * @author PC
 */
public class UserTest {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        //insert
        
//        UserDTO u1 = new UserDTO("TLN01","Le Nhat Tung", "AD","khongcopass");
//        userDAO.create(u1);
//        for(int i = 0; i <10; i++){
//            UserDTO u = new UserDTO("USER"+i,"Nguyen Van "+i, "US","PASS"+i);
//            userDAO.create(u);
//        }
//        
//         UserDTO u2 = new UserDTO("TLN01", "Le Nhat Tung", "US", "Nothing");
//        userDAO.update(u2);

//          userDAO.delete("TLN01");

//            List<UserDTO> l1 = userDAO.readAll();
//            for (UserDTO u : l1){
//                System.out.println(u);
//            }

        System.out.println(userDAO.readById("USER1"));
    }
}
