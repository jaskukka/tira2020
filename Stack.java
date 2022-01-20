public class Stack {
    
    // Size of array
    private int N;
    // Array for the objects
    private Object[] A;
    // Number of items in the stack, also used for finding the top
    // top==n-1
    private int n;

    // Constructor, eh
    public Stack() {
        N = 1;
        n = 0;
        A = new Object[N];
    }

    // Is the thing empty or nah?
    public boolean isEmpty() {
        return n == 0;
    }

    // What is the size of this one
    public int stackSize() {
        return n;
    }

    // Add an object
    public void push(Object x) {
        if (N == n) {
            N = N*2;
            Object[] temp = new Object[N];
            for (int i = 0;i < A.length; i++) {
                temp[i] = A[i];
                A[i] = null;
            }
            A = temp;
            temp = null;
        }
        A[n++] = x;
    }

    // Remove the topmost object of the stack, and return it
    public Object pop() {
        if (n == 0) {
            return null;
        }
        else {
            Object removed = A[n-1];
            Object[] temp;
            A[n-1] = null;
            n--;
            if (n == N/4 && N >= 2) {
                N = N/2;
                temp = new Object[N];
                for (int i = 0; i < A.length/2; i++) {
                    temp[i] = A[i];
                }
                this.A = temp;
            }
            return removed;
        }
    }

    // What is the first object?
    public Object peek() {
        return A[n-1];
    }
}