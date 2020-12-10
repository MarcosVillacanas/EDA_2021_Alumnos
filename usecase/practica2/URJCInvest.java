package usecase.practica2;

import material.Position;
import material.tree.narytree.LinkedTree;

import java.util.*;

public class URJCInvest {

    private class OrganizationChart {

        private class Employee implements Position<String>{
            String company;
            String employeeName;
            String employeeJob;
            String employeeDescription;

            public Employee(String company, String employeeName, String employeeJob, String employeeDescription) {
                this.company = company;
                this.employeeName = employeeName;
                this.employeeJob = employeeJob;
                this.employeeDescription = employeeDescription;
            }

            @Override
            public String getElement() { return employeeName; }

        }

        String companyName;
        LinkedTree<Employee> employees;
        HashMap<String, Position<Employee>> mapEmployees;
        HashMap<String, List<Position<Employee>>> mapJobs;

        public OrganizationChart(String companyName) {
            this.companyName = companyName;
            this.employees = new LinkedTree<>();
            this.mapEmployees = new HashMap<>();
            this.mapJobs = new HashMap<>();
        }

        public LinkedTree<Employee> getEmployees() { return employees; }

        public HashMap<String, Position<Employee>> getMapEmployees() { return mapEmployees; }

        public HashMap<String, List<Position<Employee>>> getMapJobs() { return mapJobs; }

        private class leavesIterator implements Iterator<Position<Employee>> {

            LinkedTree<Employee> tree;
            Queue<Position<Employee>> queue;

            public leavesIterator(OrganizationChart company) {
                this.tree = company.getEmployees();
                this.queue = new LinkedList<>();
                this.queue.add(this.tree.root());
            }

            @Override
            public boolean hasNext() {
                return !this.queue.isEmpty();
            }

            @Override
            public Position<Employee> next() {

                Position<Employee> next;

                do {
                    next = this.queue.poll();
                    for (Position<Employee> child : this.tree.children(next)) {
                        this.queue.add(child);
                    }
                } while (!this.tree.isLeaf(next));

                return next;
            }
        }

        public leavesIterator iterator () { return new leavesIterator(this); }
    }

    HashMap<String, OrganizationChart> companies;

    public URJCInvest() {
        this.companies = new HashMap<>();
    }

    public HashMap<String, OrganizationChart> getCompanies() { return companies; }

    public void setCompanies(HashMap<String, OrganizationChart> companies) { this.companies = companies; }

    public OrganizationChart searchCompany (String companyName) throws RuntimeException{
        if (this.companies.containsKey(companyName)) {
            return this.companies.get(companyName);
        }
        throw new RuntimeException("There is no company with such name");
    }

    public Iterable<? extends Position<OrganizationChart.Employee>> getGrantHolders (String companyName) {

        OrganizationChart company = this.searchCompany(companyName);
        LinkedList<Position<OrganizationChart.Employee>> grantHolders = new LinkedList<>();
        Iterator<Position<OrganizationChart.Employee>> ite = company.iterator();

        while (ite.hasNext()) {
            grantHolders.add(ite.next());
        }

        return grantHolders;
    }

    public Iterable<? extends Position<OrganizationChart.Employee>> getChiefs
            (String companyName, String employeeName) throws RuntimeException {

        OrganizationChart company = this.searchCompany(companyName);
        if (company.getMapEmployees().containsKey(employeeName)) {

            Position<OrganizationChart.Employee> employee = company.getMapEmployees().get(employeeName);
            LinkedList<Position<OrganizationChart.Employee>> superiors = new LinkedList<>();

            while (!employee.equals(company.getEmployees().root())) {
                employee = company.getEmployees().parent(employee);
                superiors.add(employee);
            }

            return superiors;
        }
        throw new RuntimeException("There is no employee with such name");
    }

    public Iterable<? extends Position<OrganizationChart.Employee>> getEmployees (String employeeJob) {

        LinkedList<Position<OrganizationChart.Employee>> employees = new LinkedList<>();
        Iterator<OrganizationChart> ite = this.getCompanies().values().iterator();

        while (!ite.hasNext()) {
            OrganizationChart next = ite.next();
            employees.addAll(next.getMapJobs().get(employeeJob));
        }

        return employees;
    }
}
