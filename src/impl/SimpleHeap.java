package impl;

/**
 * Heap based on k-ary tree (array-based)
 */
public class SimpleHeap<E extends Comparable<E>> {

    private E[] dataArray;
    private int degree;
    private int capacity;
    private int size;

    /**
     * @param degree - the max number of children for each node
     */
    public SimpleHeap(int degree) {
        this.degree = degree;
        this.size = 0;
        this.capacity = degree + 1;
        this.dataArray = (E[]) new Comparable[capacity];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(E data) {
        if (size == capacity - 1) {
            resize(capacity * degree + 1);
        }

        dataArray[size] = data;
        upheap(size);

        size++;
    }

    public boolean delete(E data) {
        if (isEmpty() || find(data) == -1) {
            return false;
        }

        int pos = find(data);
        swap(pos, size - 1);
        dataArray[size - 1] = null;
        size--;
        downheap(pos);

        return true;
    }

    public E deleteMax() {
        E max = dataArray[0];
        delete(max);

        return max;
    }

    private void upheap(int pos) {
        if (getParent(pos) != null && getParent(pos).compareTo(dataArray[pos]) < 0) {
            swap((pos + 1) / degree - 1, pos);
            upheap((pos + 1) / degree - 1);
        }
    }

    private void downheap(int pos) {
        E[] children = getChildren(pos);

        if (children.length != 0) {
            int positionOfMaxChild = pos * degree + 1 + maxChild(children);

            if (dataArray[positionOfMaxChild].compareTo(dataArray[pos]) > 0) {
                swap(positionOfMaxChild, pos);
                downheap(positionOfMaxChild);
            }
        }
    }

    private int maxChild(E[] children) {
        int positionOfMax = 0;

        for (int i = 1; i < children.length; i++) {
            if (children[i].compareTo(children[positionOfMax]) > 0) {
                positionOfMax = i;
            }
        }
        return positionOfMax;
    }

    private void swap(int pos1, int pos2) {
        E tmp = dataArray[pos1];
        dataArray[pos1] = dataArray[pos2];
        dataArray[pos2] = tmp;
    }

    public int find(E data) {
        for (int i = 0; i < size; i++) {
            if (dataArray[i].equals(data)) {
                return i;
            }
        }
        return -1;
    }

    public E getChild(int node, int index) {
        if (node >= size
                || node < 0
                || dataArray[node] == null
                || index >= degree
                || index < 0) {
            return null;
        }

        return dataArray[node * degree + 1 + index];
    }

    public E[] getChildren(int node) {
        E[] tmp = (E[]) new Comparable[degree];

        int j = 0;
        for (int i = node * degree + 1; i < node * degree + 1 + degree; i++) {
            if (dataArray[i] != null) {
                tmp[j] = dataArray[i];
                j++;
            }
        }

        if (j - 1 < degree) {
            E[] children = (E[]) new Comparable[j];
            for (int i = 0; i < j; i++) {
                children[i] = tmp[i];
            }
            return children;
        }

        return tmp;
    }

    public E getParent(int pos) {
        if (pos <= 0 || pos >= capacity) {
            return null;
        }
        return dataArray[(pos + 1) / degree - 1];
    }

    private void resize(int newCapacity) {
        E[] tmp = (E[]) new Comparable[newCapacity];

        for (int i = 0; i < capacity; i++) {
            tmp[i] = dataArray[i];
        }

        dataArray = tmp;
        capacity = newCapacity;
    }

    /**
     * Sort an array
     *
     * @param arrayToSort - current array
     * @param condition   - "asc" or "desc"
     * @return - sorted array
     */
    public E[] sort(E[] arrayToSort, String condition) {
        for (E e : arrayToSort) {
            insert(e);
        }

        E[] sortedArray = (E[]) new Comparable[size];

        int constantSize = size;
        for (int i = 0; i < constantSize; i++) {
            sortedArray[i] = dataArray[0];
            delete(dataArray[0]);
        }

        // Ascending sort
        if (condition.toLowerCase().equals("asc")) {
            dataArray = sortedArray;
            for (int i = 0; i < (sortedArray.length) / 2; i++)
                swap(i, dataArray.length - 1 - i);

            sortedArray = dataArray;
        }

        for (int i = 0; i < arrayToSort.length; i++) {
            arrayToSort[i] = sortedArray[i];
        }

        return arrayToSort;
    }

    public void printHeap() {
        for (int i = 0; i < size; i++) {
            System.out.print(dataArray[i] + " ");
        }
    }

}