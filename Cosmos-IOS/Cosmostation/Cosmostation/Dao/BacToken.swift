//
//  BnbToken.swift
//  Cosmostation
//
//  Created by yongjoo on 01/10/2019.
//  Copyright © 2019 wannabit. All rights reserved.
//

import Foundation


let BAC_TOKEN_TYPE_BEP2 = 1
let BAC_TOKEN_TYPE_MINI = 2

public class BacToken {
    var symbol: String = ""
    var original_symbol: String = ""
    var decimal: Int = 0
    var owner: String = ""
    var total_supply: String = ""
    var description: String = ""
    var mintable: Bool = false
    
    
    var type:Int?
    
    init() {}
    
    init(_ dictionary: [String: Any]) {
        self.symbol = dictionary["outer_name"] as? String ?? ""
        self.original_symbol = dictionary["inner_name"] as? String ?? ""
        self.decimal = dictionary["precision"] as? Int ?? 6
        self.owner = dictionary["owner_address"] as? String ?? ""
        self.total_supply = dictionary["supply_num"] as? String ?? ""
        self.description = dictionary["description"] as? String ?? ""
        self.mintable = dictionary["mintable"] as? Bool ?? false
    }
}
