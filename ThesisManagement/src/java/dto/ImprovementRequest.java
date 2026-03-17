/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author ADMIN
 */
public class ImprovementRequest {
    private String focusAnalysis;
    private String generalObservations;
    private String topPrior ;
    private String aiRequestPrompt;
    private int thesisId;
    
    public ImprovementRequest() {
    }

    public ImprovementRequest(String focusAnalysis, String generalObservations, String topPrior, String aiRequestPrompt, int thesisId) {
        this.focusAnalysis = focusAnalysis;
        this.generalObservations = generalObservations;
        this.topPrior = topPrior;
        this.aiRequestPrompt = aiRequestPrompt;
        this.thesisId = thesisId;
    }





    public String getFocusAnalysis() {
        return focusAnalysis;
    }

    public void setFocusAnalysis(String focusAnalysis) {
        this.focusAnalysis = focusAnalysis;
    }

    public String getGeneralObservations() {
        return generalObservations;
    }

    public void setGeneralObservations(String generalObservations) {
        this.generalObservations = generalObservations;
    }

    public String getTopPrior() {
        return topPrior;
    }

    public void setTopPrior(String topPrior) {
        this.topPrior = topPrior;
    }

    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public String getAiRequestPrompt() {
        return aiRequestPrompt;
    }

    public void setAiRequestPrompt(String aiRequestPrompt) {
        this.aiRequestPrompt = aiRequestPrompt;
    }
    
    
}
