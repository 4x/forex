using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
namespace fxcmAPI
{
    class Program
    {
        static FXCore.CoreAut mCore;
        static FXCore.TradeDeskAut mDesk;

        static void Main(string[] args)
        {
            bool success=false;
            while (!success)
            {
                try
                {
                    Console.WriteLine("Login...");
                    //--------------------------------------------------------------------------------
                    //create o2go object and login
                    //--------------------------------------------------------------------------------
                    mCore = new FXCore.CoreAut();
                    mDesk = (FXCore.TradeDeskAut)mCore.CreateTradeDesk("trader");
                    mDesk.Login(args[0], args[1], "http://www.fxcorporate.com", "Real");

            
                    //--------------------------------------------------------------------------------
                    //collect all required information
                    //--------------------------------------------------------------------------------
                    FXCore.TableAut acct = (FXCore.TableAut)mDesk.FindMainTable("accounts");
                    FXCore.TableAut offer = (FXCore.TableAut)mDesk.FindMainTable("offers");

                    string account_id = (string)acct.CellValue(1, "AccountID");
                    int unit_size = int.Parse(args[4]) * 1000;


                    string instrument = args[2];
                    double ask = (double)offer.CellValue(1, "Ask");
                    double bid = (double)offer.CellValue(1, "Bid");
                    bool isBuy = true;
                    if (args[3].Equals("S"))
                    {
                        isBuy = false;
                    }

                    //--------------------------------------------------------------------------------
                    //create a long trade and a pair of entry orders which acts as a stop and limit.
                    //limit is 100 pips above the market and stop is 100 pips below the market.
                    //--------------------------------------------------------------------------------

                    //1. open a trade
                    object trade_order_id, di;
                    mDesk.OpenTrade(account_id, instrument, isBuy, unit_size, 0, "", 0, 0, 0, 0, out trade_order_id, out di);

                    //finalizing
                    mDesk.Logout();
                    Console.WriteLine("Trade Successful");
                    success = true;
                    //Console.ReadLine();
                }
                catch (Exception e)
                {
                    Console.WriteLine("{0}", e.ToString());
                }
            }
        }
    }
}
