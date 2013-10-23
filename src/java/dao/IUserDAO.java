/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.User;

/**
 *
 * @author base
 */
public interface IUserDAO {
     public User getUserByID(String id);
}
