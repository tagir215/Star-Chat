package com.android.starchat.util;

import com.android.starchat.ui.uiStart.Country;
import java.util.Arrays;

public class Sort {

    public static Country[] mergeSortNonRecursive(Country[] countries){
        return nonRecursive(countries);
    }
    public static Country[] mergeSortRecursive(Country[] countries){
        return recursive(countries);
    }

    private static Country[] nonRecursive(Country[] countries){
        Country[][]sortedArrays = new Country[10000][];
        Country[][]sortedArrays2;
        for(int i=0; i< countries.length; i++){
            sortedArrays[i] = new Country[]{countries[i]};
        }
        while(sortedArrays[0].length< countries.length){
            int i=0;
            int next=0;
            sortedArrays2 = new Country[10000][];
            while(sortedArrays[next]!=null){
                if(sortedArrays[next+1]!=null){
                    sortedArrays2[i] = merge(sortedArrays[next],sortedArrays[next+1]);
                }else{
                    sortedArrays2[i]=sortedArrays[next];
                }
                i++;
                next+=2;
            }
            sortedArrays = sortedArrays2;
        }
        return sortedArrays[0];
    }

    private static Country[] recursive(Country[]countries){
        if(countries.length==1)
            return countries;
        int pivot = countries.length/2;
        Country[]left = Arrays.copyOfRange(countries,0,pivot);
        Country[]right = Arrays.copyOfRange(countries,pivot,countries.length);
        return merge(recursive(left), recursive(right));
    }


    public static Country[] merge(Country[] left, Country[] right){

        Country[] result = new Country[left.length+ right.length];
        int next = 0;
        int nextR = 0;
        int nextL = 0;
        while (next<result.length){
            if(nextL>= left.length){
                while (next< result.length){
                    result[next] = right[nextR];
                    nextR++;
                    next++;
                }
                break;
            }

            Country cl = left[nextL];

            while (next< result.length){
                if(nextR>=right.length){
                    while (next< result.length){
                        result[next] = left[nextL];
                        nextL++;
                        next++;
                    }
                    break;
                }

                Country cr = right[nextR];


                if(isA_beforeB(cr,cl)){
                    result[next]=cr;
                    next++;
                    nextR++;
                }
                else{
                    result[next]=cl;
                    next++;
                    nextL++;
                    break;
                }
            }
        }

        return result;
    }



    public static boolean isA_beforeB(Country a, Country b){
        if(a==null)
            return true;
        if(b==null)
            return false;

        char cA = a.getName().charAt(0);
        char cB = b.getName().charAt(0);
        int lengthA = a.getName().length();
        int lengthB = b.getName().length();

        int i=1;
        while (cA == cB){
            if(i>lengthA-1)
                return true;
            if(i>lengthB-1)
                return false;

            cA = a.getName().charAt(i);
            cB = b.getName().charAt(i);
            i++;
        }

        return cB > cA;
    }
}
