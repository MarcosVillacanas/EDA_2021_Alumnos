package material.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapTest {
    public static void main(String[] args) {
        Map<Integer, String> m = new HashTableMapLP<>();
        m.put(18, "A");
        m.put(41, "B");
        m.put(22, "C");
        m.put(44, "D");

        String val = m.put(18, "E");
        System.out.println(val);

        Iterator<Entry<Integer, String>> it = m.iterator();
        while (it.hasNext()) {
            Entry<Integer, String> x = it.next();
            System.out.println(x.getKey() + " - " + x.getValue());
        }

        // ejercicio de examen de asociar una matrícula a varias multas
        // el codigo este es nonsense pero vale para darse cuenta de que los mapas se pueden usar
        // como diccionarios con otras estructuras, siendo listas, árboles...

        Map<Integer, List<String>> multas = new HashTableMapLP<>();
        List<String> lMultas = new ArrayList<>();

        Integer matricula = 1;
        List<String> lx = multas.get(matricula);

        if (lx == null) {
            lMultas.add("multa 1");
        }
        else {
            lMultas.addAll(lx);
        }
        multas.put(matricula, lMultas);

    }
}
