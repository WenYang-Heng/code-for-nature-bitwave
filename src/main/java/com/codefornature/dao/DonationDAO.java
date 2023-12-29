package com.codefornature.dao;

import com.codefornature.ConnectionManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DonationDAO {
    private double donateAmount;
    private String organisation;
    private String username;
    private double donateAmountToProject;

    public DonationDAO(int donateAmount, String organisation, String username){
        this.donateAmount = donateAmount * 0.9;
        this.organisation = organisation;
        this.username = username;
        this.donateAmountToProject = donateAmount * 0.1;
    }

    public boolean insertDonation(int user_id) throws SQLException {
        String query = "INSERT INTO donations(amount, donation_date, organisation, user_id) VALUES(?, curdate(), ?, ?)";
        int rowsUpdated;
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setDouble(1, donateAmount);
                ps.setString(2, organisation);
                ps.setInt(3, user_id);
                rowsUpdated = ps.executeUpdate();
            }
        }
        return rowsUpdated > 0;
    }

    public void writeDonationToFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/assets/text/Donations.txt", true))) {
            String donationRecord = String.format("%s has donated $%.2f to %s%n", username, donateAmount, organisation);
            writer.write(donationRecord);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
