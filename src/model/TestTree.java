package model;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertSame;

public class TestTree {

    private AVLTree<Integer> tree;

    public TestTree(){
        tree = new AVLTree<Integer>();

    }

    @Test
    public void addTest(){

        tree.add(30);
        tree.add(26);
        tree.add(68);

        //it is checked that it returns true when the tree is created
        assertTrue("add does not work well", tree.add(45));

        //It is checked that it returns true when the tree is modified.
        assertTrue("add does not work well", tree.add(53));

        //It is checked that it returns false when the tree is not modified.
        assertFalse("add does not work well", tree.add(45));

    }
    @Test
    public void removeTest(){

        //It is deleted an element in an empty tree must return false
        assertFalse("remove does not work well", tree.remove(7));

        //It is deleted a non-existent element in an empty tree must return false
        tree.add(787);
        assertFalse("remove does not work well", tree.remove(4));

        //the root of a tree is deleted, it should return true
        assertTrue("remove does not work well", tree.remove(787));
        assertFalse("remove does not work well", tree.contains(787));


        //It is deleted the root of a tree with more than one node
        tree.add(34);
        tree.add(100);
        tree.add(14);
        assertTrue("remove does not work well", tree.remove(100));
        assertFalse("remove does not work well", tree.contains(100));

    }
}
