package avem.fxml;

import avem.core.AVEquipment;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;

import java.util.HashSet;
import java.util.function.Consumer;

/**
 *  This is a modified code to fit into AVEM's implementation needs.
 *
 *  Credits to this code from SalomonBrys on github.
 *
 *  SalomonBrys/MultiSelectTableView.java
 *  <script src="https://gist.github.com/SalomonBrys/8039ecdbbfc0fa465c60e948d0abe8da.js"></script>
 *
 * TableView that allows multi-selection by default (without holding the control key)
 * @see TableView
 * @param <S> The type of the objects contained within the TableView items list.
 */
public class MultiSelectTableView<S> extends TableView<S> {

    private boolean update = false;

    private HashSet<Integer> selectedIndices = new HashSet<>();
    private AVEquipment[] selectedItems = new AVEquipment[]{};
    private int lastSelectedIndex = -1;
    private int previousSelectedIndex = -1;
    private ObservableList<S> avSelectedItems;

    private HashSet<Integer> preSelectedItems;

    /**
     * @see TableView#TableView()
     */
    public MultiSelectTableView() {
        super();
        init();
    }

    /**
     * @see TableView#TableView(ObservableList)
     */
    public MultiSelectTableView(ObservableList<S> items) {
        super(items);
        init();
    }

    private void init() {
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (update || newValue == null)
                return ;

            updateSelection(sm -> {
                Integer newIndex = newValue.intValue();
                previousSelectedIndex = lastSelectedIndex;
                lastSelectedIndex = newIndex;

                toggle(newIndex);

                Platform.runLater(this::makeSelection);
            });
        });

        setOnMouseClicked(event -> {
            if (event.isShiftDown() && previousSelectedIndex != -1) {
                updateSelection(sm -> {
                    for (int i = previousSelectedIndex + 1; i < lastSelectedIndex; ++i)
                    {
                        toggle(i);
                    }
                    makeSelection();
                });
            }
            if (event.isControlDown()) {
                selectedIndices.clear();
                selectedIndices.addAll(getSelectionModel().getSelectedIndices());
            }
        });

    }

    protected void toggle(Integer index) {
        if (!selectedIndices.remove(index))
            selectedIndices.add(index);
    }

    private void makeSelection() {
        getSelectionModel().clearSelection();
        selectedIndices.forEach(index -> getSelectionModel().select(index));
    }

    /**
     * You should always use this method when planning to update the selection.
     *
     * For example: <code>tableView.updateSelection(sm -> sm.clearAll());</code>
     *
     * @param c A function that will mutate the selection.
     */
    public void updateSelection(Consumer<TableViewSelectionModel<S>> c) {
        update = true;
        TableViewSelectionModel<S> sm = getSelectionModel();
        c.accept(sm);
        Platform.runLater(() -> {
            sm.select(null);
            selectedIndices.clear();
            selectedIndices.addAll(sm.getSelectedIndices());
            avSelectedItems = sm.getSelectedItems();
            update = false;
        });
    }

    public ObservableList<S> getSelectedHashItems() {
        return avSelectedItems;
    }


}