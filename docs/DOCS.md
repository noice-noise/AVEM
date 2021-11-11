# Configure JavaFX Libraries and VM Options for IntelliJ
---

## Adding JavaFX Library
Set-up JavaFX Library SDK
File>Project Structure>Libraries>"+">New Project Library>(Find and paste the "lib" directory of your JavaFX openjfx(javafxsdk).

Sample:
`C:\Program Files\Java\openjfx-16_windows-x64_bin-sdk\javafx-sdk-16\lib`

## VM Options
Run>Edit Configurations>Paste the code from VM Options
Note: Add the path to JavaFX SDK "lib"
--module-path "****PATH TO JAVAFX SDK***" --add-modules javafx.controls,javafx.fxml

Sample:
`--module-path "C:\Program Files\Java\openjfx-16_windows-x64_bin-sdk\javafx-sdk-16\lib" --add-modules javafx.controls,javafx.fxml`

### Reference
from **Brocode ** Youtube tutorial  