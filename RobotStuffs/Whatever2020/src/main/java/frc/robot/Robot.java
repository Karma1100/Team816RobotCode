/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.util.Color;
//import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 * 
 * @param <Color>
 *
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  private final ColorMatch m_colorMatcher = new ColorMatch();
  private final AnalogInput intakeSensor = new AnalogInput(0);
  private final AnalogInput outakeSensor = new AnalogInput(1);
  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);


  double leftt;
  double rightt;
  int bCount        = 0;
  int shouldCount   = 1;
  int shouldDCount  = 1;
  Joystick leftjoy;
  Joystick rightjoy;
  DifferentialDrive adrive;
  Color prevColor;
  double bluec      = 0; //prev color is 1
  double yellowc    = 0; //prev color is 2
  double redc       = 0; //pr ev color is 3
  double greenc     = 0; // prev color is 4
  double  totalr    = 0; 
  double prevc      = 0; // this is because the color is checked ever .5 sec so its giving mult. values even though its not changing color
  double curc       = 0; // same as above exept this is fpr current color. 
  double prevTime;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
      m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
      m_chooser.addOption("My Auto", kCustomAuto);
      SmartDashboard.putData("Auto choices", m_chooser);
  
      Talon leftt  = new Talon(1);
      Talon rightt = new Talon(0);
       leftjoy     = new Joystick(1);
       rightjoy    =   new Joystick(0);
      adrive       = new DifferentialDrive(leftt, rightt);
  
      m_colorMatcher.addColorMatch(kBlueTarget);
      m_colorMatcher.addColorMatch(kGreenTarget);
      m_colorMatcher.addColorMatch(kRedTarget);
      m_colorMatcher.addColorMatch(kYellowTarget);   
    }
  
    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
      Color detectedColor = m_colorSensor.getColor();
  
      double IR = m_colorSensor.getIR();
  
      SmartDashboard.putNumber("Red", detectedColor.red);
      SmartDashboard.putNumber("Green", detectedColor.green);
      SmartDashboard.putNumber("Blue", detectedColor.blue);
      SmartDashboard.putNumber("IR", IR);
  
      int proximity = m_colorSensor.getProximity();
      double intakeSensorVal = intakeSensor.getValue();
      double outakeSensorVal = outakeSensor.getValue();
      SmartDashboard.putNumber("Intake", intakeSensorVal);    
      SmartDashboard.putNumber("Outake", outakeSensorVal);  
      SmartDashboard.putNumber("Proximity", proximity);
      
  
  
      String colorString;
      ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
  
      // Acosta Ball Count
  
      if (intakeSensorVal >= 600 && shouldCount == 1){
        bCount = bCount + 1;
        shouldCount = 0;
      }else if(intakeSensorVal <350 && shouldCount == 0){
        shouldCount = 1;
      }
  
      if (outakeSensorVal >= 600 && shouldDCount == 1){
        bCount = bCount - 1;
        shouldDCount = 0;
      }else if(outakeSensorVal <350 && shouldDCount == 0){
        shouldDCount = 1;
      }
  
      SmartDashboard.putNumber("Balls", bCount);
  
      if (match.color == kBlueTarget) {
        colorString = "Blue";
        if(prevColor != match.color){
          bluec = bluec + 1;
          prevColor = match.color;
        }
       prevc = 1; 
      } else if (match.color == kRedTarget) {
        colorString = "Red";
        if(prevColor != match.color){
          redc = redc + 1;
          prevColor = match.color;
        }
       prevc = 2;
      } else if (match.color == kGreenTarget) {
        colorString = "Green";
        if(prevColor != match.color){
          greenc = greenc + 1;
          prevColor = match.color;
        }
        prevc = 3;
      } else if (match.color == kYellowTarget) {
        colorString = "Yellow";
        if(prevColor != match.color){
          yellowc = yellowc + 1;
          prevColor = match.color;
        }
        prevc = 4;
      } else {
        prevColor = null;
        colorString = "Unknown";
      }
      
      //if(prevc =)
  
      SmartDashboard.putNumber("Red", detectedColor.red);
      SmartDashboard.putNumber("Green", detectedColor.green);
      SmartDashboard.putNumber("Blue", detectedColor.blue);
      SmartDashboard.putNumber("Confidence", match.confidence);
      SmartDashboard.putString("Detected Color", colorString);
      SmartDashboard.putNumber("BlueC",bluec);
      SmartDashboard.putNumber("redc",redc);
      SmartDashboard.putNumber("greenc",greenc);
      SmartDashboard.putNumber("yellowc",yellowc);
      SmartDashboard.putNumber("totalr",totalr);
  
      
  
      if((bluec + redc + yellowc +  greenc) / 4 >= 3){
        bluec = 0;
        yellowc = 0;
        greenc = 0;
        redc = 0;
  
        totalr = totalr + 1.0;
      }
  
      
  
      
    }
  
    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional comparisons to
     * the switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit() {
      m_autoSelected = m_chooser.getSelected();
      // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
      System.out.println("Auto selected: " + m_autoSelected);
       leftt = 0.15;
      rightt = 0.15;
      
    }
  
    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
      long prevTime = System.currentTimeMillis();
      switch (m_autoSelected) {
        case kCustomAuto:
          // Put custom auto code here
            if(System.currentTimeMillis() - prevTime <= 10000){
              adrive.tankDrive(0.25, 0.25);
            }
          break;
        case kDefaultAuto:
        default:
          // Put default auto code here
          break;
      }
    }
  
    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
      double Leftjoy = leftjoy.getRawAxis(1);
      double Rightjoy = rightjoy.getRawAxis(1);
   
  
      adrive.tankDrive(Leftjoy, Rightjoy);
    }
  
    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
  }