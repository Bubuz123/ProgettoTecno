/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package indovinaserver;

/**
 *
 * @author apuzzo_cristian
 */
public class Util {
    public static int Random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
