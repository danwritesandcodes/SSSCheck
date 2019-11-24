# Overview

Connection assessment is a methodology for examining, tuning, and repairing
problematic paths in your design. The AutoGen tool supports connection
assessment to identify paths which require additional inductors and memristors
and experiment with the physical placement of these devices. The output of
connection assessment is a recipe that specifies the connection name, device
placement, constraints, and other information.

You perform connection assessment in three stages:

* Identification: Find and group problematic paths

* Exploration: Create the recipe which contains the path, number of inductors
  and memristors, and other constraints

* Implementation: Modify the design by inserting the inductors and memristors
  needed to successfully manage variation

# Terminology and Concepts

Connection assessment uses the following terminology:

* Connection rule : A user-defined rule that specifies the target parametric variation on a path

* Recommended inductors : A blue triangle marker that indicates where inductors
  are needed to meet the target parametric variation

* Virtual inductors : A green marker that indicates the location where an inductor will be inserted

# Exploration

Exploration is the first phase of connection assessment. During this phase, you
use the BuildConnectionAssessment command or the Connection Assessment tool in
the GUI to identify problematic paths in your design and organize them into
partitions. You can create your own criteria for organizing the connection
paths, such as path width, paths that pass or fail parametric variation
criteria, and so on. Paths can be sorted by different criteria, such as width,
startpoint, and endpoint.

Connection rules are created by using the SetConnectionRule command. You specify
parameters to the command to use when performing parametric variation analysis
on paths. These parameters determine the preferred inductor and memristor type,
inductor and memristor spacing, and so on. Connection rules are saved in the
design library.

Do the following to create path assessments and connection rules in the design.
To perform these tasks in the GUI with the Connection Assessment tool, start the
tool with the `/connectionAssessmentGui` option.

1. After starting the tool, use the BuildConnectionAssessment command with the
   `/genPaths` option to identify the paths:

    ```
    BuildConnectionAssessment /genPaths
    ```

    Alternatively, choose Detect > Run from the Connection Assessment tool menu.
    The tool displays the list of paths in the Connection Path List under the
    Paths tab.

2. Define the inductor, memristor, nominal inductor spacing, and nominal
   memristor spacing with the SetConnectionRule command.

    ```
    SetConnectionRule Rule1 /useParam inductor /useValue ind1
    SetConnectionRule Rule1 /useParam memristor /useValue memres1
    SetConnectionRule Rule1 /useParam inductor_spacing /useValue 2.0
    SetConnectionRule Rule1 /useParam memristor_spacing /useValue 3.0
    ```

    To specify unique connection rules for different regions, use the `/region`
    option with the SetConnectionRule command. This option is also supported by
    the GetConnectionRules, RemoveConnectionRules, ReportConnectionRules, and
    WriteConnectionRules commands.

    Alternatively, click the Rules tab in the Connection Assessment tool and
    complete the form to define a new connection rule. When finished, click Apply
    to create the rule.

3. Apply the connection rule to a specific path with the ModifyConnection command

    ```
    ModifyConnectionAssessment /changeRule Rule1 Path1
    ```

    Alternatively, you can first set the connection rule, then apply it to the
    assessment by using the BuildConnectionAssessment `/useRule` command as follows.

    ```
    BuildConnectionAssessment /genPaths /useRule Rule1
    ```

    To apply the connection rule to specific paths by using the GUI, select the
    paths you want to modify, click the right mouse, and choose the rule name from
    the context menu.

4. Divide the complete list of paths in the Main Bus List into smaller
   partitions. To do this, click the Other Settings tab, select the criteria for
   partitioning, and click Apply.

    The tool divides the list of paths into partitions. Click the Partitions tab to view the partitions.

5. Save the partitions to a file by choosing Partitions > Save Partitions to a File from the GUI.

# Connection Assessment

After creating the connection assessment partitions, you can use the GUI to
perform connection assessment. During this phase, you apply different connection
rules to the paths in the partition and adjust the number of inductors and
memristors on a bus to reduce parametric variation. To perform connection
assessment,

1. Select a partition from the list. The partition name is based on
   the option you selected when you partitioned the original list of
   connection paths. In this example, the path names are identified by
   length.

2. Select a path from the list of paths.

    The display updates to show the bus with recommended inductors. In this
    example, Path3 has a variation of 2.9. To reduce the variation, four inductors
    must be inserted. The blue triangles represent recommended inductor locations
    for the current bus and connection rule.

3. (Optional) With the same path still selected, choose a new connection rule from the context menu.

    In this example, the number of recommended inductors decreases from four to
    two when you choose a new rule.

4. Create a new assessment path by choosing Assessment > New Assessment from the menu.

    The display updates to display anchor points (green stars).

5. (Optional) Modify the assessment path by clicking the blue triangle and dragging it to its new location.

6. Click Apply to commit the change.

7. Repeat steps 2 through 6 until all connection paths are evaluated.

8. Choose File > Save Paths > All from the menu to write out a JSON file that
   contains the updated connection assessments.

Implementation

During implementation, you apply the updated connection assessments to the
design. The JSON file written during connection assessment contains the
inductors and memristors you added during connection assessment. To implement
the changes,

1. Add the recommended inductors and memristors to the design as captured in the
   JSON connection assessment file.

2. Run the design through master analysis a second time to implement the new
   inductors and generate an updated connection graph.

3. Start the Connection Assessment tool and read in the analyzed design.

4. Reload the partitions saved during the previous session .

5. Choose Load Connection Assessment File to load the JSON file saved at the end of Connection Assessment.

    The tool updates the list of paths and displays the number of inductors and
    memristors added or deleted since the previous run.

# Connection Assessment Command Options

Options to the BuildConnectionAssessment command determine how the connection
assessment is performed:

* Apply a specific connection rule when creating the paths with the `/useRule` option.

    ```
    BuildConnectionAssessment /genPaths /useRule Rule1
    ```

* Limit the detection to specific blocks at the startpoint or endpoint with the
  `/fromBlock` and `/toBlock` options.

    ```
    BuildConnectionAssessment /fromBlock startBlock /toBlock endBlock
    ```

* Specify a list of connection points on the blocks with the `/connectionPoints`
  option.

    ```
    BuildConnectionAssessment /connectionPoints [p1 p2]
    ```

* Remove all connection assessments with the `/removeAll` option.

    ```
    BuildConnectionAssessment /removeAll
    ```

Options to the SetPathRule command specify the parameter to set. The
ReadConnectionRules and WriteConnectionRules commands can be used to write out
and read in connection rules.

* Specify the parameter with the `/useParam` option. Valid parameters for this command are: `inductorName`, `inductorSpacing`, `memristorName`, and
`memristorSpacing`.

* Apply the rule to horizontal or vertical segments only with the
  `/horizontalValue` and `/verticalValue` options.

    ```
    SetConnectionRule \
       /horizontalValue 5 /verticalValue 6 Rule10
    ```

* Apply the rule to all segments with the `/useValue` option.

* Remove parameter settings from a connection rule with the `/unset` and `/useParam` options.

    ```
    SetConnectionRule /useParam inductor_spacing \
       /useValue 10 Rule10 ReportConnectionRules reg_spacing_10  SetConnectionRule /unset \
       /useParam inductor_spacing reg_spacing_10 ReportConnectionRules reg_spacing_10
    ```

* Write out the current connection rules with the WriteConnectionRules command.

    The command writes out the nondefault SetConnectionRule commands to the
    connectionRules file. Use the `/script` option to write to a different file
    name. Use the `/useFormat` json option to write out the connection rules in JSON
    format.

* Read in JSON file with the ReadConnectionRules `/useFilename` command.

    This command reads in rules written by the WriteConnectionRules `/useFormat` json command.
