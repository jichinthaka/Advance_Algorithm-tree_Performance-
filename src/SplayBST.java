import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/******************************************************************************
 *  Compilation:  javac SplayBST.java
 *  Execution:    java SplayBST
 *  Dependencies: none
 *
 *  Splay tree. Supports splay-insert, -search, and -delete.
 *  Splays on every operation, regardless of the presence of the associated
 *  key prior to that operation.
 *
 *  Written by Josh Israel.
 *
 *  Modified by Buddhi Ayesha, Yasas Senarath
 *
 ******************************************************************************/


public class SplayBST<Key extends Comparable<Key>, Value> {

    private Node root;   // root of the BST

    // BST helper node data type
    private class Node {
        private Key key;            // key
        private Value value;        // associated data
        private Node left, right;   // left and right subtrees

        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    // return value associated with the given key
    // if no such value, return null
    public Value get(Key key) {
        root = splay(root, key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) return root.value;
        else return null;
    }

    /***************************************************************************
     *  Splay tree insertion.
     ***************************************************************************/
    public void put(Key key, Value value) {
        // splay key to root
        if (root == null) {
            root = new Node(key, value);
            return;
        }

        root = splay(root, key);

        int cmp = key.compareTo(root.key);

        // Insert new node at root
        if (cmp < 0) {
            Node n = new Node(key, value);
            n.left = root.left;
            n.right = root;
            root.left = null;
            root = n;
        }

        // Insert new node at root
        else if (cmp > 0) {
            Node n = new Node(key, value);
            n.right = root.right;
            n.left = root;
            root.right = null;
            root = n;
        }

        // It was a duplicate key. Simply replace the value
        else {
            root.value = value;
        }

    }

    /***************************************************************************
     *  Splay tree deletion.
     ***************************************************************************/
    /* This splays the key, then does a slightly modified Hibbard deletion on
     * the root (if it is the node to be deleted; if it is not, the key was
     * not in the tree). The modification is that rather than swapping the
     * root (call it node A) with its successor, it's successor (call it Node B)
     * is moved to the root position by splaying for the deletion key in A's
     * right subtree. Finally, A's right child is made the new root's right
     * child.
     */
    public void remove(Key key) {
        if (root == null) return; // empty tree

        root = splay(root, key);

        int cmp = key.compareTo(root.key);

        if (cmp == 0) {
            if (root.left == null) {
                root = root.right;
            } else {
                Node x = root.right;
                root = root.left;
                splay(root, key);
                root.right = x;
            }
        }

        // else: it wasn't in the tree to remove
    }


    /***************************************************************************
     * Splay tree function.
     * **********************************************************************/
    // splay key in the tree rooted at Node h. If a node with that key exists,
    //   it is splayed to the root of the tree. If it does not, the last node
    //   along the search path for the key is splayed to the root.
    private Node splay(Node h, Key key) {
        if (h == null) return null;

        int cmp1 = key.compareTo(h.key);

        if (cmp1 < 0) {
            // key not in tree, so we're done
            if (h.left == null) {
                return h;
            }
            int cmp2 = key.compareTo(h.left.key);
            if (cmp2 < 0) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            } else if (cmp2 > 0) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null)
                    h.left = rotateLeft(h.left);
            }

            if (h.left == null) return h;
            else return rotateRight(h);
        } else if (cmp1 > 0) {
            // key not in tree, so we're done
            if (h.right == null) {
                return h;
            }

            int cmp2 = key.compareTo(h.right.key);
            if (cmp2 < 0) {
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null)
                    h.right = rotateRight(h.right);
            } else if (cmp2 > 0) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }

            if (h.right == null) return h;
            else return rotateLeft(h);
        } else return h;
    }


    /***************************************************************************
     *  Helper functions.
     ***************************************************************************/

    // height of tree (1-node tree has height 0)
    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }


    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return 1 + size(x.left) + size(x.right);
    }

    // right rotate
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    // left rotate
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }
    
    
    public List<Long> readData(String path) throws FileNotFoundException, IOException {
        File file_i_s1_d1 = new File(path);
 
        BufferedReader br = new BufferedReader(new FileReader(file_i_s1_d1));

        String st;
        List<Long> dataStack = new ArrayList<Long>();
        while ((st = br.readLine()) != null)
        {
            //System.out.println(st);
            String stringArray[]= st.split(",");
            for(String s : stringArray)
            {
                //System.out.print(s + " ");
                dataStack.add(Long.parseLong(s));
            }
            //System.out.print("\n");
        }
        
        
        return dataStack;
    }
    
    private void insertSearchDelete_time(String insertDataPath,
            String searchDataPath, String deleteDataPath) throws IOException {
        
        SplayBST<Long, Integer> bst_s1_d1 = new SplayBST<>();
        //int[] timeArray = new int[3];
        
        {
            
            List<Long> data_i = bst_s1_d1.readData(insertDataPath);
            List<Long> data_s = bst_s1_d1.readData(searchDataPath);
            List<Long> data_d = bst_s1_d1.readData(deleteDataPath);

            long insert_Time;
            long search_Time;
            long delete_Time;
            //insert data time cal
            {
                //start
                long insert_StartTime = System.nanoTime();

                //insert data
                for(int i = 0; i < data_i.size(); i++)
                {
                    bst_s1_d1.put(data_i.get(i), 1);
                }

                //end
                long insert_EndTime = System.nanoTime();

                //time elapsed
                insert_Time = insert_EndTime - insert_StartTime;

                System.out.println("insert time in microseconds: " + insert_Time / 1000);
            }

            //search data time cal
            {
                //start
                long search_StartTime = System.nanoTime();

                //search data
                for(int i = 0; i < data_s.size(); i++)
                {
                    boolean iscontain = bst_s1_d1.contains(data_s.get(i));
                    //System.out.print(data_s.get(i));
                    //System.out.print(" is contain: ");
                    //System.out.print(iscontain);
                    //System.out.print("\n");
                }

                //end
                long search_EndTime = System.nanoTime();

                //time elapsed
                search_Time = search_EndTime - search_StartTime;

                System.out.println("search time in microseconds: " + search_Time / 1000);
            }

            System.out.print("__________________________________");
            System.out.print("\n");

            //delete data time cal
            {
                //start
                long delete_StartTime = System.nanoTime();

                //delete data
                for(int i = 0; i < data_d.size(); i++)
                {
                    bst_s1_d1.remove(data_d.get(i));
                    //System.out.print(data_d.get(i));
                    //System.out.print(" is dleted ");
                    //System.out.print("\n");
                }

                //end
                long delete_EndTime = System.nanoTime();

                //time elapsed
                delete_Time = delete_EndTime - delete_StartTime;

                System.out.println("delete time in microseconds: " + delete_Time / 1000);
            }
        }
        //return null;        
    }

    // test client
    public static void main(String[] args) throws IOException {
        SplayBST<Long, Integer> bst = new SplayBST<>();
        
        
        // set1 - data_1
        System.out.println("set1 - data_1");
        System.out.println("-------------");
        {
            String path_i = "data\\insert\\set1\\data_1.txt";
            String path_s = "data\\search\\set1\\data_1.txt";
            String path_d = "data\\delete\\set1\\data_1.txt";
            
            bst.insertSearchDelete_time(path_i, path_s, path_d);
        }
        System.out.println("\n");
        
        // set1 - data_2
        System.out.println("set1 - data_2");
        System.out.println("-------------");
        {
            String path_i = "data\\insert\\set1\\data_2.txt";
            String path_s = "data\\search\\set1\\data_2.txt";
            String path_d = "data\\delete\\set1\\data_2.txt";
            
            bst.insertSearchDelete_time(path_i, path_s, path_d);
        }
        System.out.println("\n");
        
        // set1 - data_3
        System.out.println("set1 - data_3");
        System.out.println("-------------");
        {
            String path_i = "data\\insert\\set1\\data_3.txt";
            String path_s = "data\\search\\set1\\data_3.txt";
            String path_d = "data\\delete\\set1\\data_3.txt";
            
            bst.insertSearchDelete_time(path_i, path_s, path_d);
        }
        System.out.println("\n");
        
        
        
        // set2 - data_1
        System.out.println("set2 - data_1");
        System.out.println("-------------");
        {
            String path_i = "data\\insert\\set2\\data_1.txt";
            String path_s = "data\\search\\set2\\data_1.txt";
            String path_d = "data\\delete\\set2\\data_1.txt";
            
            bst.insertSearchDelete_time(path_i, path_s, path_d);
        }
        System.out.println("\n");

        
        // set2 - data_2
        System.out.println("set2 - data_2");
        System.out.println("-------------");
        {
            String path_i = "data\\insert\\set2\\data_2.txt";
            String path_s = "data\\search\\set2\\data_2.txt";
            String path_d = "data\\delete\\set2\\data_2.txt";
            
            bst.insertSearchDelete_time(path_i, path_s, path_d);
        }
        System.out.println("\n");

        
        // set2 - data_3
        System.out.println("set2 - data_3");
        System.out.println("-------------");
        {
            String path_i = "data\\insert\\set2\\data_3.txt";
            String path_s = "data\\search\\set2\\data_3.txt";
            String path_d = "data\\delete\\set2\\data_3.txt";
            
            bst.insertSearchDelete_time(path_i, path_s, path_d);
        }
        System.out.println("\n");
        
        
    }

}