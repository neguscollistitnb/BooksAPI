import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test {

    public static void main(String[] args) {
        int nums[] = {4, 3, 6, 1, 2 , 1, 6, 3, 1};

        Map<Integer, Integer> map = new HashMap<>();
        Map<Integer, Integer> pairMap = new HashMap<>();

        Arrays.stream(nums).

        Set<Integer> keys = map.keySet();
        int sum = 0;
        for(Integer key : keys){
            sum = sum +  map.get(key) / 2;
        }

        System.out.println(sum);


    }

}
