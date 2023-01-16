import java.util.ArrayList;

class SideChannelAttack{

    public static void main(String[] args)
    {
        try
        {
            if(args.length ==3)
            {
                //we need to make sure that there is enough of a warkmup for the cache to be accurate.
                int steps = Integer.parseInt(args[0]) * 1024 * 1024;
                //the size of cache being tested. 1024 bytes or 1kb
                int testArraySize = Integer.parseInt(args[1]) * 1024 * 1024;
                //number or iterations per cache size to complete
                int numIterations = Integer.parseInt(args[2]);
                //the test cache size assumed 1mb smalles requred for modern machines
                int testCacheSize = 1024 * 1024;
                //increment th cache size being tested by 1b every run through till we reach the max size the array can support
                int cacheIncrement = 1024 * 1024;
                //two variables to store the memory access speeds.
                long endTime = 0;
                long startTime = 0;
                long ellapsedTime = 0;
                long currLongTime = 0;
                //lists to store the most significant runtimes(longest) that would indicate a cache miss.
                ArrayList<Integer> bestList = new ArrayList<>(); 
                ArrayList<Long> bestTimes = new ArrayList<>();
                //the array initialized that will be accessed for time measuments.
                byte[] arr = new byte[testArraySize];
                //usefull variables pre initializd.
                float size = 0;
                int i = 0;
                int count = 0;
                int modu = 0;

                //run through every 1mb increment till we reach the max size we wish to test.
                while(testCacheSize < testArraySize)
                {
                    //calculate a number that can be used in a binary modulus to ensure we are only accesing the amount of memory for this test
                    modu = testCacheSize - 1;
                    //the number of tests per cache size we want to go through.
                    for(count = 0; count < numIterations; count++)
                    {
                        //run the memory access, and record the times
                        startTime = System.nanoTime();
                        for(i = 0; i < steps; i++)
                        {
                            arr[(i * 64) & modu] &= 1;
                        }
                        endTime = System.nanoTime();
                        ellapsedTime = endTime - startTime;
                        //to MB format for printing purposes.
                        size = testCacheSize;
                        size = size/1024/1024;
                        //this is how we distingush a cache miss, if the time is at least double that of the previous access.
                        if((ellapsedTime/1.5) > currLongTime && currLongTime != 0)
                        {
                            bestList.add(Math.round(size));
                            bestTimes.add(ellapsedTime);
                        }
                        currLongTime = ellapsedTime;
                        System.out.println(steps + " iterations time:" + ellapsedTime + "ns test cache size: " + size +"MB");
                    }
                    //increment to the next size that is to be tested.
                    testCacheSize = testCacheSize + cacheIncrement;
                } 
                System.out.println("L3 Cache Size found: " + Math.round(getBestEstimate(bestList, bestTimes)) + "MB");
            } else {
                System.out.println("Syntax Error, usage: java SideChannelAttack <num steps> <test array size> <consecutive runs for each cahche size tested>");
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    public static long getBestEstimate(ArrayList<Integer> _bestList, ArrayList<Long> _bestTimes)
    {
        //track the index of the largest time.
        int best = 0;
        long longestTime = 0;
        //for every time that was significant, loop through and keep track of the longest time.
        for(int c = 0; c < _bestList.size(); c++)
        {
            if(_bestTimes.get(c) > longestTime)
            {
                longestTime = _bestTimes.get(c);
                best = c;
            }
        }
        //retrun the corresponding cache size to the longest run time. 
        return _bestList.get(best);
    }
}