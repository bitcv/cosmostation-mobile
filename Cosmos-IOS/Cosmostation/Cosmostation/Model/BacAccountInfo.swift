//
//  BacAccountInfo.swift
//  Cosmostation
//
//  Created by yongjoo on 27/09/2019.
//  Copyright © 2019 wannabit. All rights reserved.
//

import Foundation

public class BacAccountInfo {
    var address: String = ""
    var account_number: UInt64 = 0
    var sequence: UInt64 = 0
    var balances: Array<BacBalance> = Array<BacBalance>()
    
    init() {}
    
    init(_ dictionary: [String: Any]) {
        self.address = dictionary["address"] as? String ?? ""
        self.account_number = dictionary["account_number"] as? UInt64 ?? 0
        self.sequence = dictionary["sequence"] as? UInt64 ?? 0
        self.balances.removeAll()
        if let rawBalances = dictionary["coins"] as? Array<NSDictionary> {
            for balance in rawBalances {
                self.balances.append(BacBalance(balance as! [String : Any]))
            }
        }
    }
    
    public class BacBalance {
        var amount: String = ""
        var denom: String = ""
        
        init() {}
        
        init(_ dictionary: [String: Any]) {
            self.amount = dictionary["amount"] as? String ?? "0"
            self.denom = dictionary["denom"] as? String ?? ""
        }
        
    }
}
