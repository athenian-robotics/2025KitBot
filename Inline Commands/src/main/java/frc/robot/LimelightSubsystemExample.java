package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.LimelightTarget_Fiducial;
import frc.robot.LimelightHelpers.PoseEstimate;

public class LimelightSubsystemExample extends SubsystemBase {
    private final String LIMELIGHT_NAME = "limelight";
    private final double VALID_TARGET_AREA_THRESHOLD = 0.1; // Minimum target area to consider valid (0-100%)
    private final double MAX_TARGET_RANGE = 6.0; // Maximum distance to trust target in meters
    
    private boolean hasValidTarget = false;
    private double lastValidTargetTimestamp = 0;
    // Constructor
    public LimelightSubsystemExample() {
        // Tell Limelight where is the camera on the robot
        /* NEEDS TO BE CONFIGUED!!!! */
        LimelightHelpers.setCameraPose_RobotSpace(LIMELIGHT_NAME,
            0.5,    // Forward offset (meters)
            0.0,    // Side offset (meters)
            0.5,    // Height offset (meters)
            0.0,    // Roll (degrees)
            30.0,   // Pitch (degrees)
            0.0     // Yaw (degrees)
        );

        // Set AprilTag pipeline (assuming pipeline 0 is configured for AprilTags)
        LimelightHelpers.setPipelineIndex(LIMELIGHT_NAME, 0);
    }

    @Override
    public void periodic() {
        // Get latest Limelight results
        updateTargetTracking();
        publishTelemetry();
    }

    /**
     * Updates target tracking status and data
     */
    private void updateTargetTracking() {
        // Get raw fiducial (AprilTag) data for detailed analysis
        LimelightHelpers.RawFiducial[] rawFiducials = LimelightHelpers.getRawFiducials(LIMELIGHT_NAME);
        
        hasValidTarget = false;
        if (rawFiducials != null && rawFiducials.length > 0) {
            for (LimelightHelpers.RawFiducial tag : rawFiducials) {
                // Check if target meets our criteria
                if (tag.ta > VALID_TARGET_AREA_THRESHOLD && 
                    tag.distToRobot < MAX_TARGET_RANGE && 
                    tag.ambiguity < 0.2) { 
                    hasValidTarget = true;
                    // Recod when we saw this valid target
                    lastValidTargetTimestamp = Timer.getFPGATimestamp();
                    break; //Valid Target
                }
            }
        }
    }

    /**
     * Gets the robot's estimated pose using MegaTag2 (requires gyro integration)
     * @param robotYaw Current robot yaw from gyro in degrees
     * @return Estimated robot pose on field
     */
    public Pose2d getEstimatedPose(double robotYaw) {
        // Update Limelight with current robot orientation
        // Tell Limelight current rotation from gyroscope (if we have one.)
        LimelightHelpers.SetRobotOrientation(LIMELIGHT_NAME, robotYaw, 0.0, 0.0, 0.0, 0.0, 0.0);
        
        // Get pose estimate using MegaTag2
        PoseEstimate poseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(LIMELIGHT_NAME);
        // Only returns the pose if we see at least one tags.
        if (poseEstimate != null && poseEstimate.tagCount >= 1) {
            return poseEstimate.pose;
        }
        return null;
    }

    /**
     * Gets targeting data for alignment with a specific AprilTag
     * @param targetTagId The ID of the AprilTag to target
     * @return Double array with [hasTarget, tx, ty, ta] or null if target not found
     */
    public double[] getTargetingData(int targetTagId) {
        // Tell Limelight which AprilTag we want to track
        // Set the priority tag ID for tracking
        LimelightHelpers.setPriorityTagID(LIMELIGHT_NAME, targetTagId);
        
        if (!hasValidTarget) {
            return null;
        }

        return new double[] {
            LimelightHelpers.getTV(LIMELIGHT_NAME) ? 1.0 : 0.0,
            LimelightHelpers.getTX(LIMELIGHT_NAME),
            LimelightHelpers.getTY(LIMELIGHT_NAME),
            LimelightHelpers.getTA(LIMELIGHT_NAME)
        };
    }

    /**
     * Calculate drive speeds for aligning with a target
     * @param targetingData Array from getTargetingData()
     * @return Double array with [forwardSpeed, strafeSpeed, rotationSpeed]
     */
    public double[] calculateAlignmentSpeeds(double[] targetingData) {
        if (targetingData == null) {
            return new double[] {0.0, 0.0, 0.0};
        }

        double tx = targetingData[1];
        double ty = targetingData[2];
        double ta = targetingData[3];

        // Simple proportional control for alignment
        double strafeSpeed = -tx * 0.03;  // Negative because positive tx means target is to the right
        double forwardSpeed = -ty * 0.03; // Adjust based on your robot's coordinate system
        double rotationSpeed = -tx * 0.02; // Helps keep target centered

        // Limit speeds
        strafeSpeed = Math.max(-0.5, Math.min(0.5, strafeSpeed));
        forwardSpeed = Math.max(-0.5, Math.min(0.5, forwardSpeed));
        rotationSpeed = Math.max(-0.3, Math.min(0.3, rotationSpeed));

        return new double[] {forwardSpeed, strafeSpeed, rotationSpeed};
    }

    // Transmit data to dashboard.
    private void publishTelemetry() {
        SmartDashboard.putBoolean("Limelight/HasTarget", hasValidTarget);
        SmartDashboard.putNumber("Limelight/LastValidTarget", 
            Timer.getFPGATimestamp() - lastValidTargetTimestamp);
        
        if (hasValidTarget) {
            SmartDashboard.putNumber("Limelight/tx", LimelightHelpers.getTX(LIMELIGHT_NAME));
            SmartDashboard.putNumber("Limelight/ty", LimelightHelpers.getTY(LIMELIGHT_NAME));
            SmartDashboard.putNumber("Limelight/ta", LimelightHelpers.getTA(LIMELIGHT_NAME));
        }
    }

    /**
     * @return Whether the limelight currently sees a valid target
     */
    public boolean hasValidTarget() {
        return hasValidTarget;
    }

    /**
     * @return Time since last valid target was seen
     */
    public double getSecondsSinceValidTarget() {
        return Timer.getFPGATimestamp() - lastValidTargetTimestamp;
    }
}