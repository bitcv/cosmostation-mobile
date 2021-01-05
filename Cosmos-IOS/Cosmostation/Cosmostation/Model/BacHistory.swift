//
//  BacHistory.swift
//  Cosmostation
//
//  Created by andrew wu on 2021/1/5.
//  Copyright © 2021 wannabit. All rights reserved.
//

import Foundation

public class BacHistory {
    var txHash: String = ""
    var blockHeight: Int64 = -1
    var txType: String = ""
    var timeStamp: String = ""
    var fromAddr: String = ""
    var toAddr: String = ""
    var amount: String = ""
    
    init() {}
    
    init(_ dictionary: [String: Any]) {
        self.txHash = dictionary["trade_hash"] as? String ?? ""
        self.blockHeight = dictionary["height"] as? Int64 ?? -1
        self.txType = dictionary["msg_type"] as? String ?? ""
        self.timeStamp = dictionary["create_time"] as? String ?? ""
        self.fromAddr = dictionary["from"] as? String ?? ""
        self.toAddr = dictionary["to"] as? String ?? ""
        self.amount = dictionary["amount"] as? String ?? ""
    }
}
