package productStates;

import strategies.restock.IRestockOperationStrategy;

public abstract class State {
    protected String name;

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }
}
