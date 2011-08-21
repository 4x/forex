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

                    string instrument = args[2];

                    if (args[5].Equals("E"))
                    {
                        mDesk.SetOfferSubscription(instrument, "Enabled");
                    }
            
                    //--------------------------------------------------------------------------------
                    //collect all required information
                    //--------------------------------------------------------------------------------
                    FXCore.TableAut acct = (FXCore.TableAut)mDesk.FindMainTable("accounts");

                    string account_id = (string)acct.CellValue(1, "AccountID");
                    int unit_size = int.Parse(args[4]) * 1000;

                    bool isBuy = true;
                    if (args[3].Equals("S"))
                    {
                        isBuy = false;
                    }

                    
                    //1. open a trade
                    object trade_order_id, di;
                    mDesk.OpenTrade(account_id, instrument, isBuy, unit_size, 0, "", 0, 0, 0, 0, out trade_order_id, out di);

                    Console.WriteLine("Trade Successful");
                    success = true;

                    Thread.Sleep(5000);

                    if (args[5].Equals("D"))
                    {
                        mDesk.SetOfferSubscription(instrument, "Disabled");
                    }
                    //finalizing
                    mDesk.Logout();
                    
                }
                catch (Exception e)
                {
                    Console.WriteLine("{0}", e.ToString());
                }
            }
        }
    }
}
