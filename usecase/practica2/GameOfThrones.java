package usecase.practica2;

import material.Position;
import material.tree.narytree.LinkedTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class GameOfThrones {

    private class FamilyMember {
        private String id;
        private String name;
        private String surname;
        private boolean isMale;
        private int age;
        private Position<FamilyMember> pos;
        private String family;

        public FamilyMember(String input) {
            String [] parts = input.split(" ");
            this.id = parts[0];
            this.name = parts[2];
            this.surname = parts[3];
            this.isMale = parts[4].charAt(1) == 'M';
            this.age = Integer.parseInt(parts[5]);
        }

        public String getId() { return id; }

        public String getName() { return name; }

        public String getSurname() { return surname; }

        public boolean isMale() { return isMale; }

        public int getAge() { return age; }

        public String getFamily() { return this.family; }

        public void setFamily(String family) { this.family = family; }

        public Position<FamilyMember> getPos() { return this.pos; }

        public void setPos(Position<FamilyMember> pos) { this.pos = pos; }
    }

    HashMap<String, LinkedTree<FamilyMember>> families;
    HashMap<String, FamilyMember> idToPerson;
    HashMap<String, String> nameToId;

    public GameOfThrones() {
        this.families = new HashMap<>();
        this.idToPerson = new HashMap<>();
        this.nameToId = new HashMap<>();
    }

    public void loadFile(String pathToFile) throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(pathToFile));
            String line = reader.readLine();
            while (!line.startsWith("---")) {

                FamilyMember p = new FamilyMember(line);

                this.idToPerson.put(p.getId(), p);
                this.nameToId.put(p.getName() + " " + p.getSurname(), p.getId());

                line = reader.readLine();
            }
            int to = Integer.parseInt(reader.readLine());
            for (int i = 0; i < to; i++) {

                String idRoot = reader.readLine();
                FamilyMember member = this.idToPerson.get(idRoot);

                LinkedTree<FamilyMember> newFamily = new LinkedTree<>();
                Position<FamilyMember> pos = newFamily.addRoot(this.idToPerson.get(idRoot));
                this.families.put(member.getSurname(), newFamily);

                member.setFamily(member.getSurname());
                member.setPos(pos);
            }
            line = reader.readLine();
            while (line != null) {

                String idParent = line.split(" ")[0];
                String idChild = line.split(" ")[2];

                FamilyMember memberParent = this.idToPerson.get(idParent);
                FamilyMember memberChild = this.idToPerson.get(idChild);

                String familyParent = memberParent.getFamily();
                Position<FamilyMember> posChild = this.families.get(familyParent).add(memberChild, memberParent.getPos());

                memberChild.setFamily(familyParent);
                memberChild.setPos(posChild);

                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public LinkedTree<FamilyMember> getFamily(String surname) throws RuntimeException {
        if (this.families.isEmpty()) {
            throw new RuntimeException("No families have been loaded yet");
        }
        if (this.families.containsKey(surname)) {
            return this.families.get(surname);
        }
        throw new RuntimeException("Surname does not match any saved family");
    }

    // corregir
    public String findHeir(String surname) throws RuntimeException{
        if (this.families.containsKey(surname)) {

            LinkedTree<FamilyMember> familyTree = this.families.get(surname);
            int olderSon = -1, olderDaughter = -1;
            Position<FamilyMember> heir = null;

            for (Position<FamilyMember> member : familyTree.children(familyTree.root())) {
                int memberAge = member.getElement().getAge();
                if (member.getElement().isMale()) {
                    if (memberAge > olderSon) {
                        heir = member;
                        olderSon = memberAge;
                    }
                } else {
                    if (memberAge > olderDaughter && olderSon == -1) {
                        heir = member;
                        olderDaughter = memberAge;
                    }
                }
            }
            if (heir == null) {
                throw new RuntimeException("There is no heritage for this family");
            }
            return heir.getElement().getName() + " " + heir.getElement().getSurname();
        }
        throw new RuntimeException("Surname does not match any saved family");
    }

    public void changeFamily(String memberName, String newParent) {

        FamilyMember member = this.idToPerson.get(this.nameToId.get(memberName));
        FamilyMember parent = this.idToPerson.get(this.nameToId.get(newParent));

        LinkedTree<FamilyMember> memberFamily = this.families.get(member.getFamily());
        LinkedTree<FamilyMember> parentFamily = this.families.get(parent.getFamily());

        Position<FamilyMember> memberOldPos = member.getPos();
        move(parent, parentFamily, member, memberFamily);

        memberFamily.remove(memberOldPos);
    }

    private void move(FamilyMember parent, LinkedTree<FamilyMember> destTree,
                      FamilyMember member, LinkedTree<FamilyMember> originTree) {

        Position<FamilyMember> memberNewPos = destTree.add(member, parent.getPos());
        Position<FamilyMember> memberOldPos = member.getPos();
        member.setPos(memberNewPos);
        member.setFamily(parent.getFamily());

        for (Position<FamilyMember> child: originTree.children(memberOldPos)) {
            move(member, destTree, child.getElement(), originTree);
        }
    }

    public boolean areFamily(String name1, String name2) {

        FamilyMember member1 = this.idToPerson.get(this.nameToId.get(name1));
        FamilyMember member2 = this.idToPerson.get(this.nameToId.get(name2));

        return member1.getFamily().equals(member2.getFamily());
    }
}
