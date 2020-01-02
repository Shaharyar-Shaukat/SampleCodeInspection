package com.intellij.codeInspection;



import com.intellij.psi.*;

import com.intellij.ui.DocumentAdapter;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;


public class SampleCodeInspection extends AbstractBaseJavaLocalInspectionTool {



    // This string holds a list of classes relevant to this inspection.
    @SuppressWarnings({"WeakerAccess"})
    @NonNls
    public String Msg = "SwingUtilities.invokeLater";


    @Override
    public JComponent createOptionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JTextField checkedClasses = new JTextField(Msg);
        checkedClasses.getDocument().addDocumentListener(new DocumentAdapter() {
            public void textChanged(DocumentEvent event) {
                Msg = checkedClasses.getText();
            }
        });
        panel.add(checkedClasses);
        return panel;
    }

    /**
     *
     * @param holder     object for visitor to register problems found.
     * @param isOnTheFly true if inspection was run in non-batch mode
     * @return non-null visitor for this inspection.
     * @see JavaElementVisitor
     */
    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            /**
             *  This string defines the short message shown to a user signaling the inspection
             *  found a problem. It reuses a string from the inspections bundle.
             */
            @NonNls
            private final String DESCRIPTION_TEMPLATE = "SDK " + InspectionsBundle.message("inspection.comparing.references.problem.descriptor");

            /**
             * Avoid defining visitors for both Reference and Binary expressions.
             *
             * @param psiReferenceExpression  The expression to be evaluated.
             */
            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {
            }


            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                super.visitMethodCallExpression(expression);
                PsiReferenceExpression methodExpression = expression.getMethodExpression();
                String MethodName = methodExpression.getQualifiedName();
                System.out.println(" MethodName= "+ MethodName);
                if (MethodName.equals("SwingUtilities.invokeLater") || MethodName.startsWith("SwingUtilities.invokeLater")) {
                    holder.registerProblem(expression,DESCRIPTION_TEMPLATE);
                }

            }

        };
    }
}
