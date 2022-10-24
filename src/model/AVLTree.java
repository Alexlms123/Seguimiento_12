package model;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class AVLTree <T> extends java.util.AbstractSet<T> {
        private Nodo<T> root;

        Comparator<T> comparator;

        public AVLTree(){
        }

        public AVLTree(Comparator<T> cmp){
            this.comparator = cmp;
        }

        public boolean add(T e) throws ClassCastException, NullPointerException, IllegalStateException{
            Nodo<T> node = new Nodo<T>(e);
            boolean exit = false;
            boolean rig = false;
            Nodo<T> rootTmp = this.getRoot();

            int altLeft, altRight;

            if(rootTmp == null){
                this.root = node;
                return true;
            }else

                if(this.contains(node.getData())){
                    return false;
                }

                else{
                    while(!exit){

                        if(this.compareData(node.getData(), rootTmp.getData())>0){
                            if(rootTmp.getRight()!=null){
                                rootTmp = rootTmp.getRight();
                            }else{
                                exit = true;
                                rig = true;
                            }

                        }
                        else{
                            if(rootTmp.getLeft()!=null){
                                rootTmp = rootTmp.getLeft();
                            }else{
                                exit = true;
                            }
                        }
                    }
                    if(rig){
                        rootTmp.setRight(node);
                    }

                    else{
                        rootTmp.setLeft(node);
                    }

                    while(balancedTree(this.getRoot())<0){
                        rootTmp = nodeFather(rootTmp);

                        if(rootTmp.getRight()==null){
                            altRight = 0;
                        }else{
                            altRight = rootTmp.getRight().getHeight();
                        }

                        if(rootTmp.getLeft()==null){
                            altLeft = 0;
                        }else{
                            altLeft = rootTmp.getLeft().getHeight();
                        }

                        Nodo<T> change = structure(rootTmp, altLeft, altRight);
                        Nodo<T> higher = nodeFather(rootTmp);

                        if(compareData(higher.getData(), rootTmp.getData())!=0){
                            if(higher.getLeft()!=null && compareData(higher.getLeft().getData(), rootTmp.getData())==0){
                                higher.setLeft(change);
                            }
                            else if(higher.getRight()!=null && compareData(higher.getRight().getData(), rootTmp.getData())==0){
                                higher.setRight(change);
                            }
                        }else{
                            this.root = change;
                        }
                    }
                    return true;
                }
        }

        private Nodo<T> structure(Nodo<T> node, int altLeft, int altRight){
            if(extractFactorE(node)==2){
                if( extractFactorE(node.getRight() )==1  || extractFactorE(node.getRight()) == 0){
                    node = leftSimpleRotation(node);
                }

                else if(extractFactorE(node.getRight() )==-1){
                    node = rightCompoundRotation(node);
                }
            }
            else if(extractFactorE(node)==-2){
                if(extractFactorE(node.getLeft() )==-1 || extractFactorE(node.getRight())==0){
                    node = rightSimpleRotation(node);
                }

                else if(extractFactorE(node.getLeft())==1){
                    node = leftCompoundRotation(node);
                }
            }

            return node;
        }
        public int extractFactorE(Nodo<T> node){
            if(node!=null){
                return node.getFactorE();
            }else{
                return 0;
            }
        }

        public Nodo<T> leftSimpleRotation(Nodo<T> node){
            Nodo<T> nodeTmp = node;

            node = nodeTmp.getRight(); //clone??
            nodeTmp.setRight(node.getLeft());

            node.setLeft(nodeTmp);

            return node;
        }

        public Nodo<T> rightSimpleRotation(Nodo<T> node){
            Nodo<T> nodeTmp = node;
            node = nodeTmp.getLeft();

            nodeTmp.setLeft(node.getRight());
            node.setRight(nodeTmp);

            return node;
        }

        public Nodo<T> leftCompoundRotation(Nodo<T> node){
            Nodo<T> nodeTmp = node;

            nodeTmp = leftSimpleRotation(nodeTmp.getLeft());

            node.setLeft(nodeTmp);

            nodeTmp = rightSimpleRotation(node);

            return nodeTmp;
        }

        public Nodo<T> rightCompoundRotation(Nodo<T> node){
            Nodo<T> nodeTmp = node;

            nodeTmp = rightSimpleRotation(nodeTmp.getRight());

            node.setRight(nodeTmp);

            nodeTmp= leftSimpleRotation(node);

            return nodeTmp;
        }

        public int balancedTree(Nodo<T> n){
            int hLft = 0;
            int hRgt = 0;

            if(n==null){
                return 0;
            }

            hLft = balancedTree(n.getLeft());

            if(hLft < 0){
                return hLft;
            }

            hRgt = balancedTree(n.getRight());

            if(hRgt <0){
                return hRgt;
            }

            if(Math.abs(hLft - hRgt)>1){
                return -1;
            }

            return Math.max(hLft, hRgt) + 1;
        }

        public Nodo<T> nodeFather(Nodo<T> node){
            Nodo<T> rootTmp = this.getRoot();
            Stack<Nodo<T>> stack = new Stack<Nodo<T>>();
            stack.push(rootTmp);
            while(rootTmp.getRight()!=null || rootTmp.getLeft()!=null){
                if(this.compareData(node.getData(), rootTmp.getData())>0){
                    if(rootTmp.getRight()!=null){
                        rootTmp = rootTmp.getRight();
                    }
                }
                else if(this.compareData(node.getData(), rootTmp.getData())<0){
                    if(rootTmp.getLeft()!=null){
                        rootTmp = rootTmp.getLeft();
                    }
                }
                if(this.compareData(node.getData(), rootTmp.getData())==0){
                    return stack.pop();
                }

                stack.push(rootTmp);
            }
            return stack.pop();
        }

        public boolean addAll(Collection<? extends T> c) throws ClassCastException, NullPointerException, IllegalStateException{
            Iterator<? extends T> iter = c.iterator();
            Iterator<? extends T> iter2 = c.iterator();
            T data, first;
            boolean insertedData = false;

            if(this.isEmpty()){
                first = iter.next();
                while(iter.hasNext()){
                    this.compareData(first, iter.next());
                }
            }

            else{
                first = this.getRoot().getData();
                while(iter.hasNext()){
                    this.compareData(first, iter.next());
                }
            }

            while(iter2.hasNext()){
                data = iter2.next();
                if(data!=null){
                    if(this.add(data)){
                        insertedData = true;
                    }
                }
            }
            return  insertedData;
        }

        public void clear(){
            Iterator<T> iter = this.iterator();

            while(iter.hasNext()){
                remove(iter.next());
            }
        }

        public boolean contains(Object o) throws ClassCastException, NullPointerException{
            Nodo<T> rootTmp = this.getRoot();
            if(this.isEmpty()){
                return false;
            }

            if(this.compareData((T)o, rootTmp.getData())==0){
                return true;
            }

            while(rootTmp.getRight()!=null || rootTmp.getLeft()!=null){

                if(this.compareData((T)o, rootTmp.getData())>0){
                    if(rootTmp.getRight()!=null){
                        rootTmp = rootTmp.getRight();
                    }else{
                        return false;
                    }
                }else if(this.compareData((T)o, rootTmp.getData())<0){
                    if(rootTmp.getLeft()!=null){
                        rootTmp = rootTmp.getLeft();
                    }else{
                        return false;
                    }
                }

                if(this.compareData((T)o, rootTmp.getData())==0){
                    return true;
                }
            }
            return false;
        }

        public boolean containsAll(Collection<?> c) throws ClassCastException, NullPointerException{
            Iterator<?> iter = c.iterator();
            List<?> treeList = this.inOrden();
            T data = null;

            if(this.isEmpty()){
                return false;
            }

            while(iter.hasNext()){
                data = (T)iter.next();

                if(!treeList.contains(data)){
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty(){
            return this.size()==0;
        }

        public Iterator<T> iterator(){
            List<T> list= this.inOrden();
            Iterator<T> iter = list.iterator();

            return iter;
        }

        public boolean remove(Object o) throws ClassCastException, NullPointerException{
            Nodo<T> erase =null,view=null,change=null, nFather = null;
            Nodo<T> rootTmp = this.getRoot();
            T c_aux, d_aux;
            boolean exit = false;
            int altRight = 0;
            int altLeft = 0;
            int a = 0;

            if(this.isEmpty()){
                return false;
            }

            if(this.compareData((T)o, rootTmp.getData())==0){
                exit = true;
                erase = rootTmp;
            }

            while(!exit && (rootTmp.getRight()!=null || rootTmp.getLeft()!=null)){

                if(this.compareData((T)o, rootTmp.getData())>0){
                    if(rootTmp.getRight()!=null){
                        rootTmp = rootTmp.getRight();
                    }else{
                        return false;
                    }
                }else if(this.compareData((T)o, rootTmp.getData())<0){

                    if(rootTmp.getLeft()!=null){
                        rootTmp = rootTmp.getLeft();
                    }else{
                        return false;
                    }
                }

                if(this.compareData((T)o, rootTmp.getData())==0){
                    exit = true;
                    erase = rootTmp;
                }
            }

            if(exit){
                view = erase;

                if(erase.getLeft()==null && erase.getRight()==null){
                    view= nodeFather(erase);
                    nFather = nodeFather(erase);

                    if(this.size()==1){
                        this.root = null;
                    }

                    if(nFather.getLeft()!=null && compareData(nFather.getLeft().getData(), erase.getData())==0){
                        nFather.setLeft(null);
                    }else if(nFather.getRight()!=null && compareData(nFather.getRight().getData(), erase.getData())==0){
                        nFather.setRight(null);
                    }

                    erase.setData(null);
                }

                else if(erase.getHeight()<=2){

                    if(erase.getLeft()!=null){
                        erase.setData(erase.getLeft().getData());
                        erase.setLeft(null);
                    }

                    else if(erase.getRight()!=null){
                        erase.setData(erase.getRight().getData());
                        erase.setRight(null);
                    }
                }

                else{

                    if(erase.getLeft()!=null){
                        change = erase.getLeft();

                        while(change.getRight()!=null){
                            change = change.getRight();
                        }
                    }

                    else if(erase.getRight()!=null){
                        change = change.getRight();

                        while(change.getLeft()!=null){
                            change = change.getLeft();
                        }
                    }

                    c_aux = change.getData();
                    Nodo<T> father = nodeFather(change);

                    //si el nodo que hemos cambiado se ha quedado con alg�n hijo...
                    if(change.getLeft()!=null || change.getRight()!=null){
                        if(change.getLeft()!=null){
                            change.setData(change.getLeft().getData());
                            change.setLeft(null);
                        }else if(change.getRight()!=null){
                            change.setData(change.getRight().getData());
                            change.setRight(null);
                        }
                    }
                    else{
                        if(father.getLeft()!=null && compareData(father.getLeft().getData(), change.getData())==0){
                            father.setLeft(null);
                        }else{
                            father.setRight(null);
                        }
                        change.setData(erase.getData());
                        erase.setData(c_aux);
                    }
                }

                while(balancedTree(this.getRoot())<0){
                    if(view.getRight()==null){
                        altRight = 0;
                    }else{
                        altRight = view.getRight().getHeight();
                    }

                    if(view.getLeft()==null){
                        altLeft = 0;
                    }else{
                        altLeft = view.getLeft().getHeight();
                    }

                    Nodo<T> change2 = structure(view, altLeft, altRight);
                    Nodo<T> higher = nodeFather(view);

                    if(compareData(higher.getData(), view.getData())!=0){
                        if(higher.getLeft()!=null && compareData(higher.getLeft().getData(), view.getData())==0){
                            higher.setLeft(change2);
                        }
                        else if(higher.getRight()!=null && compareData(higher.getRight().getData(), view.getData())==0){
                            higher.setRight(change2);
                        }
                    }else{
                        this.root = change2;
                    }
                    view = nodeFather(view);
                }
                return true;
            }
            return false;
        }

        public boolean removeAll(Collection<?> c) throws ClassCastException, NullPointerException{
            T data;
            boolean unerased = false;

            Iterator<?> iter = c.iterator();
            while(iter.hasNext()){
                data = (T)iter.next();

                if(remove(data)){
                    unerased = true;
                }
            }

            return unerased;
        }

        public boolean retainAll(Collection<?> c) throws ClassCastException, NullPointerException{
            List<T> treeList = this.preOrden();
            List<T> eraseList = new ArrayList<T>();
            T data;
            boolean modifiedData = false;

            for(int i=0; i<treeList.size(); i++){
                if(!c.contains(treeList.get(i))){
                    eraseList.add(treeList.get(i));
                }
            }

            Iterator<?> iter = eraseList.iterator();
            while(iter.hasNext()){
                modifiedData = true;
                data = (T)iter.next();

                remove(data);
            }

            return modifiedData;
        }

        public int size(){
            return this.preOrden().size();
        }

        public List<T> inOrden(){
            List<T> list = new ArrayList<T>();
            Nodo<T> node = this.getRoot();
            Stack<Nodo<T>> stack = new Stack<Nodo<T>>();

            while((node!=null && node.getData()!=null)|| !stack.empty()){
                if(node!=null){
                    stack.push(node);
                    node = node.getLeft();
                }else{
                    node = stack.pop();
                    list.add(node.getData());
                    node = node.getRight();
                }
            }

            return list;
        }

        public List<T> preOrden(){
            List<T> list = new ArrayList<T>();
            Nodo<T> node = this.getRoot();
            Stack<Nodo<T>> stack = new Stack<Nodo<T>>();

            while((node!=null && node.getData()!=null) || !stack.empty()){
                if(node!=null){
                    list.add(node.getData());
                    stack.push(node);
                    node = node.getLeft();
                }else{
                    node = stack.pop();
                    node = node.getRight();
                }
            }

            return list;
        }

        public List<T> postOrden(){
            List<T> list = new ArrayList<T>();
            Nodo<T> node = this.getRoot();
            Stack<Nodo<T>> stack1 = new Stack<Nodo<T>>();
            Stack<Boolean> stack2 = new Stack<Boolean>();

            while((node!=null && node.getData()!=null) || !stack1.empty()){

                if(node!=null){
                    stack1.push(node);
                    stack2.push(true);
                    node = node.getLeft();
                }else{
                    node = stack1.pop();
                    if(stack2.pop()){
                        stack1.push(node);
                        stack2.push(false);
                        node = node.getRight();
                    }else{
                        list.add(node.getData());
                        node = null;
                    }
                }
            }

            return list;
        }

        public Nodo<T> getRoot(){
            return this.root;
        }

        public Nodo<T> getNode(T data){
            Nodo<T> rootTmp = this.getRoot();

            if(this.isEmpty()){
                return null;
            }

            while(rootTmp.getRight()!=null || rootTmp.getLeft()!=null){

                if(this.compareData(data, rootTmp.getData())>0){
                    if(rootTmp.getRight()!=null){
                        rootTmp = rootTmp.getRight();
                    }else{
                        return null;
                    }
                }else if(this.compareData(data, rootTmp.getData())<0){
                    if(rootTmp.getLeft()!=null){
                        rootTmp = rootTmp.getLeft();
                    }else{
                        return null;
                    }
                }

                if(this.compareData(data, rootTmp.getData())==0){
                    return rootTmp;
                }
            }

            return rootTmp;
        }

        private Comparator<T> getComparator(){
            return this.comparator;
        }

        public T extractData(Nodo<T> node){
            return node.getData();
        }

        private int compareData(T t1, T t2){
            if(this.comparator==null){
                return ((Comparable)t1).compareTo(t2);
            }else{
                return this.comparator.compare(t1,t2);
            }
        }

        private class Nodo<T>{
            private T data;

            private Nodo<T> left;

            private Nodo<T> right;

            private int factorE;

            public Nodo(){
                data = null;
                left = null;
                right = null;
                factorE = 0;
            }

            public Nodo(T data){
                this.data = data;
                left = null;
                right = null;
                factorE = 0;
            }

            public Nodo<T> getLeft(){
                return left;
            }
            public Nodo<T> getRight(){
                return right;
            }

            public T getData(){
                return data;
            }

            public void setRight(Nodo<T> right){
                this.right = right;
            }

            public void setLeft(Nodo<T> left){
                this.left = left;
            }

            public void setData(T data){
                this.data = data;
            }

            public int getFactorE(){
                int altRgt = 0;
                int altLft = 0;
                if(this.getRight()!=null){
                    altRgt = this.getRight().getHeight();
                }
                if(this.getLeft()!=null){
                    altLft = this.getLeft().getHeight();
                }
                return (altRgt - altLft);
            }

            public void setFactorE(int fe){
                this.factorE = fe;
            }

            public int getHeight(){
                int hLft = 0;
                int hRgt = 0;

                if(this.getData()==null){
                    return 0;
                }


                if(this.getLeft()!=null){
                    hLft = this.getLeft().getHeight();
                }else{
                    hLft = 0;
                }

                if(this.getRight()!=null){
                    hRgt = this.getRight().getHeight();
                }else{
                    hRgt = 0;
                }
                return Math.max(hLft, hRgt) + 1;
            }
        }
}
