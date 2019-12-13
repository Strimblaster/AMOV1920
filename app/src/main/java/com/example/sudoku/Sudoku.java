package com.example.sudoku;

import java.io.Serializable;

public class Sudoku extends Thread implements Serializable {
    private Grid grid;

    public Sudoku()  { }

//        HashMap<Integer,Boolean> hashMap =  grid.getCellHints(3,7);
//        Iterator iterator = hashMap.entrySet().iterator();
//        System.out.println("Hints: ");
//        while (iterator.hasNext()) {
//            Map.Entry<Integer,Boolean> me2 = (Map.Entry) iterator.next();
//           if (me2.getValue().booleanValue()){
//               System.out.println(me2.getKey()+"");
//           }
//        }

    public Integer[] getList(){
        int size = grid.getSize();
        Integer list[] = new Integer[size * size] ;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size; j++) {
                list[(i*size)+j] = grid.getValue(i,j);
            }
        }
        return list;
    }

    @Override
    public void run() {
        this.grid = new Grid(Difficulty.random);
    }
}
