package material.test.practica1;

import material.linear.LinkedQueue;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

class LinkedQueueTest {
    LinkedQueue<Integer> queue;
    final int MAX=25;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        queue = new LinkedQueue<>();
        for (int i = 0; i < MAX; i++) {
            queue.enqueue(i);
        }
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        queue = null;
    }

    @org.junit.jupiter.api.Test
    void testSize() {
        assertEquals(queue.size(), MAX);
    }

    @org.junit.jupiter.api.Test
    void testIsEmpty() {
        assertFalse(queue.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void testFront() {
        assertEquals((int) queue.front(), 0);
    }

    @org.junit.jupiter.api.Test
    void testEnqueue() {
        int size = queue.size();
        for (int i=1;i<=10;i++) {
            queue.enqueue(i);
            size++;
            assertEquals(size, queue.size());
        }
    }

    @org.junit.jupiter.api.Test
    void testDequeue() {
        int actual = 0;
        while(!queue.isEmpty()){
            int element = (int) queue.dequeue();
            assertEquals(element,actual);
            actual++;
        }
        assertTrue(queue.isEmpty());
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> queue.dequeue());
        assertEquals("Queue is empty", runtimeException.getMessage());

    }
}